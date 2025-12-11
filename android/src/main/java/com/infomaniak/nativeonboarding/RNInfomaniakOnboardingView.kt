package com.infomaniak.nativeonboarding

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.infomaniak.core.auth.UserExistenceChecker
import com.infomaniak.core.auth.models.UserLoginResult
import com.infomaniak.core.auth.utils.LoginFlowController
import com.infomaniak.core.auth.utils.LoginUtils
import com.infomaniak.core.crossapplogin.back.BaseCrossAppLoginViewModel
import com.infomaniak.core.crossapplogin.back.BaseCrossAppLoginViewModel.AccountsCheckingState
import com.infomaniak.core.crossapplogin.back.ExternalAccount
import com.infomaniak.core.crossapplogin.front.previews.AccountsCheckingStatePreviewParameter
import com.infomaniak.core.network.NetworkConfiguration
import com.infomaniak.lib.login.InfomaniakLogin
import com.infomaniak.nativeonboarding.models.AnimatedIllustration
import com.infomaniak.nativeonboarding.models.LoginConfiguration
import com.infomaniak.nativeonboarding.models.OnboardingArgumentColors
import com.infomaniak.nativeonboarding.models.OnboardingConfiguration
import com.infomaniak.nativeonboarding.models.Page
import com.infomaniak.nativeonboarding.models.StaticIllustration
import com.infomaniak.nativeonboarding.preview.PagesPreviewParameter
import com.infomaniak.nativeonboarding.theme.OnboardingTheme
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView
import kotlinx.coroutines.launch

private const val SUCCESS_EVENT_KEY = "accessTokens"
private const val ERROR_EVENT_KEY = "error"


class CrossAppLoginViewModelFactory(
    private val applicationId: String,
    private val clientId: String,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CrossAppLoginViewModel(applicationId, clientId) as T
}


class RNInfomaniakOnboardingView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
    // Creates and initializes an event dispatcher for the `onLoginSuccess` and `onLoginError` events.
    // The name of the event is inferred from the value and needs to match the event name defined in the module.
    private val onLoginSuccess by EventDispatcher()
    private val onLoginError by EventDispatcher()

    private var loginData: LoginData? by mutableStateOf(null)
    private val pages = mutableStateListOf<Page>()
    private var onboardingArgumentColors by mutableStateOf<OnboardingArgumentColors?>(null)

    private val userExistenceChecker = UserExistenceChecker { false }

    private var areButtonsLoading by mutableStateOf(false)

    init {
        addView(ComposeView(context).apply {
            setContent {
                val scope = rememberCoroutineScope()
                val snackbarHostState = remember { SnackbarHostState() }

                loginData?.let { loginData ->
                    val crossAppLoginViewModel = viewModel<CrossAppLoginViewModel>(
                        factory = CrossAppLoginViewModelFactory(loginData.applicationId, loginData.clientId)
                    )

                    val hostActivity = LocalActivity.current as ComponentActivity
                    LaunchedEffect(crossAppLoginViewModel) {
                        crossAppLoginViewModel.activateUpdates(hostActivity, singleSelection = true)
                    }

                    val accounts by crossAppLoginViewModel.accountsCheckingState.collectAsStateWithLifecycle()
                    val skippedIds by crossAppLoginViewModel.skippedAccountIds.collectAsStateWithLifecycle()

                    Log.i(TAG, "Got ${accounts.checkedAccounts.count()} accounts from other apps")

                    val loginFlowController = LoginUtils.rememberLoginFlowController(
                        infomaniakLogin = loginData.infomaniakLogin,
                        userExistenceChecker = userExistenceChecker,
                    ) { userLoginResult ->
                        when (userLoginResult) {
                            is UserLoginResult.Success -> reportAccessToken(userLoginResult.user.apiToken.accessToken)
                            is UserLoginResult.Failure -> scope.launch { snackbarHostState.showSnackbar(userLoginResult.errorMessage) }
                            null -> Unit
                        }

                        if (userLoginResult !is UserLoginResult.Success) stopLoadingLoginButtons()
                    }

                    OnboardingViewContent(
                        pages = pages,
                        colors = { onboardingArgumentColors },
                        accounts = { accounts },
                        skippedIds = { skippedIds },
                        isLoginButtonLoading = { areButtonsLoading },
                        isSignUpButtonLoading = { areButtonsLoading },
                        onLoginRequest = { accounts ->
                            val account = accounts.singleOrNull()
                            if (account == null) {
                                openLoginWebView(loginFlowController)
                            } else {
                                scope.launch { connectSelectedAccount(account, crossAppLoginViewModel, snackbarHostState) }
                            }
                        },
                        onCreateAccount = { openAccountCreation(loginFlowController, loginData) },
                        onSaveSkippedAccounts = { crossAppLoginViewModel.skippedAccountIds.value = it },
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
        })
    }

    fun setOnboardingConfig(config: OnboardingConfiguration) {
        pages.removeAll { true }
        pages.addAll(config.slides)

        pages.forEach { page ->
            page.backgroundImage.reportErrors()

            when (val illustration = page.animatedIllustration ?: page.staticIllustration) {
                is AnimatedIllustration -> illustration.reportErrors()
                is StaticIllustration -> illustration.reportErrors()
                null -> reportMissingIllustration()
            }
        }

        onboardingArgumentColors = OnboardingArgumentColors(
            primaryColorLight = config.primaryColorLight.toColor(),
            primaryColorDark = config.primaryColorDark.toColor(),
            onPrimaryColorLight = config.onPrimaryColorLight.toColor(),
            onPrimaryColorDark = config.onPrimaryColorDark.toColor(),
        )
    }

    fun setLoginConfig(config: LoginConfiguration?) {
        config?.let {
            NetworkConfiguration.init(
                appId = it.redirectURIScheme,
                appVersionCode = it.appVersionCode,
                appVersionName = it.appVersionName,
            )

            loginData = LoginData(
                InfomaniakLogin(
                    context = context,
                    loginUrl = it.loginUrl,
                    clientID = it.clientId,
                    appUID = it.redirectURIScheme,
                    accessType = null,
                ),
                clientId = it.clientId,
                applicationId = it.redirectURIScheme,
                createAccountUrl = it.createAccountUrl,
                successHost = it.successHost,
                cancelHost = it.cancelHost,
            )
        }
    }

    private fun openLoginWebView(loginFlowController: LoginFlowController) {
        startLoadingLoginButtons()
        loginFlowController.login()
    }

    private fun openAccountCreation(loginFlowController: LoginFlowController, loginData: LoginData) {
        startLoadingLoginButtons()
        loginFlowController.createAccount(loginData.createAccountUrl, loginData.successHost, loginData.cancelHost)
    }

    private suspend fun connectSelectedAccount(
        account: ExternalAccount,
        viewModel: BaseCrossAppLoginViewModel,
        snackbarHostState: SnackbarHostState,
    ) {
        startLoadingLoginButtons()
        val loginResult = viewModel.attemptLogin(selectedAccounts = listOf(account))
        loginUsers(loginResult, snackbarHostState)
        loginResult.errorMessageIds.forEach { messageResId -> snackbarHostState.showSnackbar(context.getString(messageResId)) }
    }

    private suspend fun loginUsers(loginResult: BaseCrossAppLoginViewModel.LoginResult, snackbarHostState: SnackbarHostState) {
        // TODO: Handle case where it failed and returned no tokens
        when (val result = LoginUtils.getLoginResultsAfterCrossApp(loginResult.tokens, context, userExistenceChecker).single()) {
            is UserLoginResult.Success -> reportAccessToken(result.user.apiToken.accessToken)
            is UserLoginResult.Failure -> {
                stopLoadingLoginButtons()
                snackbarHostState.showSnackbar(result.errorMessage)
            }
        }
    }

    private fun startLoadingLoginButtons() {
        areButtonsLoading = true
    }

    private fun stopLoadingLoginButtons() {
        areButtonsLoading = false
    }

    private fun reportAccessToken(accessToken: String) {
        onLoginSuccess(mapOf(SUCCESS_EVENT_KEY to accessToken))
    }

    private fun reportError(errorMessage: String) {
        onLoginError(mapOf(ERROR_EVENT_KEY to errorMessage))
    }

    private fun reportMissingAsset(fileName: String) {
        reportError("Missing file '$fileName' in Android assets folders")
    }

    private fun reportMissingIllustration() {
        reportError("Missing illustration. Provide either a StaticIllustration or an AnimatedIllustration instance in each slide")
    }

    private fun AnimatedIllustration.reportErrors() {
        if (fileName.assetFileExists(context).not()) reportMissingAsset(fileName)
    }

    private fun StaticIllustration.reportErrors() {
        if (androidDarkFileName.assetFileExists(context).not()) reportMissingAsset(androidDarkFileName)
        if (androidLightFileName.assetFileExists(context).not()) reportMissingAsset(androidLightFileName)
    }

    companion object {
        private val TAG = RNInfomaniakOnboardingView::class.java.simpleName
    }
}

private fun String.toColor(): Color = Color(toColorInt())

@Composable
private fun OnboardingViewContent(
    pages: SnapshotStateList<Page>,
    colors: () -> OnboardingArgumentColors?,
    accounts: () -> AccountsCheckingState,
    skippedIds: () -> Set<Long>,
    isLoginButtonLoading: () -> Boolean,
    isSignUpButtonLoading: () -> Boolean,
    onLoginRequest: (List<ExternalAccount>) -> Unit,
    onCreateAccount: () -> Unit,
    onSaveSkippedAccounts: (Set<Long>) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    OnboardingTheme(colors) {
        OnboardingScreen(
            pages = pages,
            accountsCheckingState = accounts,
            skippedIds = skippedIds,
            isLoginButtonLoading = isLoginButtonLoading,
            isSignUpButtonLoading = isSignUpButtonLoading,
            onLoginRequest = onLoginRequest,
            onCreateAccount = onCreateAccount,
            onSaveSkippedAccounts = onSaveSkippedAccounts,
            snackbarHostState = snackbarHostState,
        )
    }
}

private data class LoginData(
    val infomaniakLogin: InfomaniakLogin,
    val clientId: String,
    val applicationId: String,
    val createAccountUrl: String,
    val successHost: String,
    val cancelHost: String,
)

@Preview
@Composable
private fun Preview(
    @PreviewParameter(PagesPreviewParameter::class) pages: SnapshotStateList<Page>,
    @PreviewParameter(AccountsCheckingStatePreviewParameter::class) accounts: AccountsCheckingState,
) {
    OnboardingViewContent(
        pages = pages,
        colors = { null },
        accounts = { accounts },
        skippedIds = { emptySet() },
        isLoginButtonLoading = { false },
        isSignUpButtonLoading = { false },
        onLoginRequest = {},
        onCreateAccount = {},
        onSaveSkippedAccounts = {},
        snackbarHostState = SnackbarHostState(),
    )
}
