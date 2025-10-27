package com.infomaniak.nativeonboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infomaniak.core.compose.basics.Typography
import com.infomaniak.core.onboarding.OnboardingPage
import com.infomaniak.core.onboarding.OnboardingScaffold
import com.infomaniak.core.onboarding.components.OnboardingComponents.DefaultBackground
import com.infomaniak.core.onboarding.components.OnboardingComponents.DefaultTitleAndDescription
import com.infomaniak.nativeonboarding.images.AppImages.AppIllus
import com.infomaniak.nativeonboarding.images.illus.OnboardingGradientLeft
import com.infomaniak.nativeonboarding.images.illus.PhoneMessageBubblesLight
import com.infomaniak.nativeonboarding.theme.KChatTheme
import com.infomaniak.nativeonboarding.theme.LocalCustomColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onLoginRequest: () -> Unit,
    onCreateAccount: () -> Unit,
) {
    val pagerState = rememberPagerState(pageCount = { Page.entries.size })
    val isLastPage by remember { derivedStateOf { pagerState.currentPage >= pagerState.pageCount - 1 } }
    val scope = rememberCoroutineScope()

    OnboardingScaffold(
        pagerState = pagerState,
        onboardingPages = Page.entries.mapIndexed { index, page -> page.toOnboardingPage(pagerState, index) },
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
        DefaultBackground(background, modifier = Modifier.padding(bottom = 300.dp))
    },
    illustration = {
        Image(illustration, contentDescription = null)
    },
    text = {
        DefaultTitleAndDescription(
            title = titleRes,
            description = description,
            titleStyle = Typography.h2.copy(color = LocalCustomColors.current.primaryTextColor),
            descriptionStyle = Typography.bodyRegular.copy(color = LocalCustomColors.current.secondaryTextColor),
        )
    }
)

private enum class Page(
    val background: ImageVector,
    val illustration: ImageVector,
    val titleRes: String,
    val description: String,
) {
    DiscussionChannel(
        background = AppIllus.OnboardingGradientLeft,
        illustration = AppIllus.PhoneMessageBubblesLight,
        titleRes = "Hello World!",
        description = "Hello World!",
    ),
    Videoconferencing(
        background = AppIllus.OnboardingGradientLeft,
        illustration = AppIllus.PhoneMessageBubblesLight,
        titleRes = "Hello World!",
        description = "Hello World!",
    ),
    Confidentiality(
        background = AppIllus.OnboardingGradientLeft,
        illustration = AppIllus.PhoneMessageBubblesLight,
        titleRes = "Hello World!",
        description = "Hello World!",
    ),
    Connection(
        background = AppIllus.OnboardingGradientLeft,
        illustration = AppIllus.PhoneMessageBubblesLight,
        titleRes = "Hello World!",
        description = "Hello World!",
    ),
}

@Preview
@Composable
private fun Preview() {
    KChatTheme {
        Surface {
            OnboardingScreen(
                onLoginRequest = {},
                onCreateAccount = {},
            )
        }
    }
}
