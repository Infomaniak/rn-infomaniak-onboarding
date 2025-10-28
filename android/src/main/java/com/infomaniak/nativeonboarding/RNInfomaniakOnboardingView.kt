package com.infomaniak.nativeonboarding

import android.content.Context
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.infomaniak.nativeonboarding.models.Page
import com.infomaniak.nativeonboarding.models.LoginConfiguration
import com.infomaniak.nativeonboarding.models.OnboardingConfiguration
import com.infomaniak.nativeonboarding.preview.PagesPreviewParameter
import com.infomaniak.nativeonboarding.theme.KChatTheme
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.viewevent.EventDispatcher
import expo.modules.kotlin.views.ExpoView

class RNInfomaniakOnboardingView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
    // Creates and initializes an event dispatcher for the `onLoginSuccess` and `onLoginError` events.
    // The name of the event is inferred from the value and needs to match the event name defined in the module.
    private val onLoginSuccess by EventDispatcher()
    private val onLoginError by EventDispatcher()

    private val pages = mutableStateListOf<Page>()

    init {
        addView(ComposeView(context).apply {
            setContent {
                OnboardingViewContent(pages)
            }
        })
    }

    fun setOnboardingConfig(config: OnboardingConfiguration?) {
        pages.removeAll { true }
        config?.let { pages.addAll(it.slides) }
    }

    fun setLoginConfig(config: LoginConfiguration?) {
        // TODO
    }
}

@Composable
private fun OnboardingViewContent(pages: SnapshotStateList<Page>) {
    KChatTheme {
        OnboardingScreen(
            pages = pages,
            onLoginRequest = {},
            onCreateAccount = {},
        )
    }
}

@Preview
@Composable
private fun Preview(@PreviewParameter(PagesPreviewParameter::class) pages: SnapshotStateList<Page>) {
    MaterialTheme {
        OnboardingViewContent(pages)
    }
}
