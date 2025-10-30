package com.infomaniak.nativeonboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.infomaniak.core.compose.basics.Typography
import com.infomaniak.core.onboarding.OnboardingPage
import com.infomaniak.core.onboarding.OnboardingScaffold
import com.infomaniak.core.onboarding.components.OnboardingComponents.DefaultTitleAndDescription
import com.infomaniak.core.onboarding.components.OnboardingComponents.ThemedDotLottie
import com.infomaniak.core.onboarding.models.OnboardingLottieSource
import com.infomaniak.nativeonboarding.models.Page
import com.infomaniak.nativeonboarding.preview.PagesPreviewParameter
import com.infomaniak.nativeonboarding.theme.LocalCustomColors
import com.infomaniak.nativeonboarding.theme.OnboardingTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    pages: SnapshotStateList<Page>,
    onLoginRequest: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val isLastPage by remember { derivedStateOf { pagerState.currentPage >= pagerState.pageCount - 1 } }
    val scope = rememberCoroutineScope()

    OnboardingScaffold(
        pagerState = pagerState,
        onboardingPages = pages.mapIndexed { index, page -> page.toOnboardingPage(pagerState, index) },
        bottomContent = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .height(150.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (isLastPage) {
                    Button({}) { Text("Connect") }
                    Button({}) { Text("New account") }
                } else {
                    Button({ scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } }) { Text("Next") }
                }
            }
        },
    )
}

@Composable
private fun Page.toOnboardingPage(pagerState: PagerState, index: Int): OnboardingPage = OnboardingPage(
    background = {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data("file:///android_asset/${backgroundImageName}")
                .build(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
    },
    illustration = {
        ThemedDotLottie(
            source = OnboardingLottieSource.Asset(illustrationName),
            isCurrentPageVisible = { pagerState.currentPage == index },
            themeId = { illustrationThemeName },
        )
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
private fun Preview(@PreviewParameter(PagesPreviewParameter::class) pages: SnapshotStateList<Page>) {
    OnboardingTheme {
        Surface {
            OnboardingScreen(
                pages = pages,
                onLoginRequest = {},
                onCreateAccount = {},
            )
        }
    }
}
