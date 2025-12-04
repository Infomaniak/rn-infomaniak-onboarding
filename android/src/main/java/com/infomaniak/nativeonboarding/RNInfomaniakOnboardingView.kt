package com.infomaniak.nativeonboarding

import android.content.Context
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
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
import com.infomaniak.core.auth.UserExistenceChecker
import com.infomaniak.core.auth.models.UserLoginResult
import com.infomaniak.core.auth.utils.LoginUtils
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

class RNInfomaniakOnboardingView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
    // Creates and initializes an event dispatcher for the `onLoginSuccess` and `onLoginError` events.
    // The name of the event is inferred from the value and needs to match the event name defined in the module.
    private val onLoginSuccess by EventDispatcher()
    private val onLoginError by EventDispatcher()

    private var loginData: LoginData? by mutableStateOf(null)
    private val pages = mutableStateListOf<Page>()
    private var onboardingArgumentColors by mutableStateOf<OnboardingArgumentColors?>(null)

    private val userExistenceChecker = UserExistenceChecker { false }

    init {
        addView(ComposeView(context).apply {
            setContent {
                loginData?.let {
                    val scope = rememberCoroutineScope()
                    val snackbarHostState = remember { SnackbarHostState() }

                    val loginFlowController = LoginUtils.rememberLoginFlowController(
                        infomaniakLogin = it.infomaniakLogin,
                        userExistenceChecker = userExistenceChecker,
                    ) { userLoginResult ->
                        when (userLoginResult) {
                            is UserLoginResult.Success -> onLoginSuccess(mapOf("accessToken" to userLoginResult.user.apiToken.accessToken))
                            is UserLoginResult.Failure -> scope.launch { snackbarHostState.showSnackbar(userLoginResult.errorMessage) }
                            null -> Unit
                        }
                    }

                    OnboardingViewContent(
                        pages = pages,
                        colors = { onboardingArgumentColors },
                        onLoginRequest = { loginFlowController.login() },
                        onCreateAccount = {
                            loginFlowController.createAccount(it.createAccountUrl, it.successHost, it.cancelHost)
                        },
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
                createAccountUrl = it.createAccountUrl,
                successHost = it.successHost,
                cancelHost = it.cancelHost,
            )
        }
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
}

private fun String.toColor(): Color = Color(toColorInt())

@Composable
private fun OnboardingViewContent(
    pages: SnapshotStateList<Page>,
    colors: () -> OnboardingArgumentColors?,
    onLoginRequest: () -> Unit,
    onCreateAccount: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    OnboardingTheme(colors) {
        OnboardingScreen(
            pages = pages,
            onLoginRequest = onLoginRequest,
            onCreateAccount = onCreateAccount,
            snackbarHostState = snackbarHostState,
        )
    }
}

private data class LoginData(
    val infomaniakLogin: InfomaniakLogin,
    val createAccountUrl: String,
    val successHost: String,
    val cancelHost: String,
)

@Preview
@Composable
private fun Preview(@PreviewParameter(PagesPreviewParameter::class) pages: SnapshotStateList<Page>) {
    OnboardingViewContent(pages, { null }, {}, {}, SnackbarHostState())
}
