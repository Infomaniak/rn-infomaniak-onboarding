package com.infomaniak.nativeonboarding

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.core.graphics.toColorInt
import com.infomaniak.nativeonboarding.models.OnboardingArgumentColors
import com.infomaniak.nativeonboarding.models.Page
import com.infomaniak.nativeonboarding.models.LoginConfiguration
import com.infomaniak.nativeonboarding.models.OnboardingConfiguration
import com.infomaniak.nativeonboarding.preview.PagesPreviewParameter
import com.infomaniak.nativeonboarding.theme.OnboardingTheme
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView

class RNInfomaniakOnboardingView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
    // Creates and initializes an event dispatcher for the `onLoginSuccess` and `onLoginError` events.
    // The name of the event is inferred from the value and needs to match the event name defined in the module.
    private val onLoginSuccess by EventDispatcher()
    private val onLoginError by EventDispatcher()

    private val pages = mutableStateListOf<Page>()
    private var onboardingArgumentColors by mutableStateOf<OnboardingArgumentColors?>(null)

    init {
        addView(ComposeView(context).apply {
            setContent {
                OnboardingViewContent(pages, { onboardingArgumentColors })
            }
        })
    }

    fun setOnboardingConfig(config: OnboardingConfiguration) {
        pages.removeAll { true }
        pages.addAll(config.slides)

        onboardingArgumentColors = OnboardingArgumentColors(
            primaryColorLight = config.primaryColorLight.toColor(),
            primaryColorDark = config.primaryColorDark.toColor(),
            onPrimaryColorLight = config.onPrimaryColorLight.toColor(),
            onPrimaryColorDark = config.onPrimaryColorDark.toColor(),
        )
    }

    fun setLoginConfig(config: LoginConfiguration?) {
        // TODO
    }
}

private fun String.toColor(): Color = Color(toColorInt())

@Composable
private fun OnboardingViewContent(pages: SnapshotStateList<Page>, colors: () -> OnboardingArgumentColors?) {
    OnboardingTheme(colors) {
        OnboardingScreen(
            pages = pages,
            onLoginRequest = {},
            onCreateAccount = {},
        )
    }
}

// fun salut() {
//     CrossLoginDefaults
// }

@Preview
@Composable
private fun Preview(@PreviewParameter(PagesPreviewParameter::class) pages: SnapshotStateList<Page>) {
    OnboardingViewContent(pages, { null })
}
