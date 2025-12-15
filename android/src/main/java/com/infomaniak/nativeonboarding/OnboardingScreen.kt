package com.infomaniak.nativeonboarding

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.infomaniak.core.crossapplogin.back.BaseCrossAppLoginViewModel.AccountsCheckingState
import com.infomaniak.core.crossapplogin.back.BaseCrossAppLoginViewModel.AccountsCheckingStatus
import com.infomaniak.core.crossapplogin.back.ExternalAccount
import com.infomaniak.core.crossapplogin.front.components.CrossLoginBottomContent
import com.infomaniak.core.crossapplogin.front.components.NoCrossAppLoginAccountsContent
import com.infomaniak.core.crossapplogin.front.previews.AccountsPreviewParameter
import com.infomaniak.core.onboarding.OnboardingPage
import com.infomaniak.core.onboarding.OnboardingScaffold
import com.infomaniak.core.onboarding.components.OnboardingComponents
import com.infomaniak.core.onboarding.components.OnboardingComponents.DefaultTitleAndDescription
import com.infomaniak.core.onboarding.components.OnboardingComponents.ThemedDotLottie
import com.infomaniak.core.onboarding.models.OnboardingLottieSource
import com.infomaniak.core.ui.compose.basics.Typography
import com.infomaniak.nativeonboarding.models.Page
import com.infomaniak.nativeonboarding.preview.PagesPreviewParameter
import com.infomaniak.nativeonboarding.theme.LocalCustomColors
import com.infomaniak.nativeonboarding.theme.OnboardingTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    pages: SnapshotStateList<Page>,
    accountsCheckingState: () -> AccountsCheckingState,
    skippedIds: () -> Set<Long>,
    isSingleSelection: Boolean,
    isLoginButtonLoading: () -> Boolean,
    isSignUpButtonLoading: () -> Boolean,
    onLoginRequest: (accounts: List<ExternalAccount>) -> Unit,
    onCreateAccount: () -> Unit,
    onSaveSkippedAccounts: (Set<Long>) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })

    OnboardingScaffold(
        pagerState = pagerState,
        onboardingPages = pages.mapIndexed { index, page -> page.toOnboardingPage(pagerState, index) },
        bottomContent = { paddingValues ->
            OnboardingComponents.CrossLoginBottomContent(
                modifier = Modifier
                    .padding(paddingValues)
                    .consumeWindowInsets(paddingValues),
                pagerState = pagerState,
                accountsCheckingState = accountsCheckingState,
                skippedIds = skippedIds,
                isLoginButtonLoading = isLoginButtonLoading,
                onContinueWithSelectedAccounts = { selectedAccounts -> onLoginRequest(selectedAccounts) },
                onUseAnotherAccountClicked = { onLoginRequest(emptyList()) },
                onSaveSkippedAccounts = onSaveSkippedAccounts,
                noCrossAppLoginAccountsContent = NoCrossAppLoginAccountsContent.accountRequired(
                    onLogin = { onLoginRequest(emptyList()) },
                    onCreateAccount = onCreateAccount,
                    isLoginButtonLoading = isLoginButtonLoading,
                    isSignUpButtonLoading = isSignUpButtonLoading,
                ),
                isSingleSelection = isSingleSelection,
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    )
}

@Composable
private fun Page.toOnboardingPage(pagerState: PagerState, index: Int): OnboardingPage = OnboardingPage(
    background = {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/${backgroundImage.fileName}")
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
    },
    illustration = {
        animatedIllustration?.let {
            ThemedDotLottie(
                source = OnboardingLottieSource.Asset(it.fileName),
                isCurrentPageVisible = { pagerState.currentPage == index },
                themeId = { it.themeName },
            )
        } ?: staticIllustration?.let {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("file:///android_asset/${it.fileName}")
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
            )
        }
    },
    text = {
        DefaultTitleAndDescription(
            title = title,
            description = subtitle,
            titleStyle = Typography.h2.copy(color = LocalCustomColors.current.primaryTextColor),
            descriptionStyle = Typography.bodyRegular.copy(color = LocalCustomColors.current.secondaryTextColor),
        )
    }
)

@Preview
@Composable
private fun Preview(
    @PreviewParameter(PagesPreviewParameter::class) pages: SnapshotStateList<Page>,
    @PreviewParameter(AccountsPreviewParameter::class) accounts: List<ExternalAccount>,
) {
    OnboardingTheme {
        Surface {
            OnboardingScreen(
                pages = pages,
                accountsCheckingState = {
                    AccountsCheckingState(status = AccountsCheckingStatus.Checking, checkedAccounts = accounts)
                },
                skippedIds = { emptySet() },
                isSingleSelection = false,
                isLoginButtonLoading = { false },
                isSignUpButtonLoading = { false },
                onLoginRequest = {},
                onCreateAccount = {},
                onSaveSkippedAccounts = {},
                snackbarHostState = SnackbarHostState(),
            )
        }
    }
}
