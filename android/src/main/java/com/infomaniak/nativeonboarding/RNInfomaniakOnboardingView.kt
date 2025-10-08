package com.infomaniak.nativeonboarding

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.infomaniak.core.onboarding.components.OnboardingComponents
import expo.modules.kotlin.AppContext
import expo.modules.kotlin.views.ExpoView

class RNInfomaniakOnboardingView(context: Context, appContext: AppContext) : ExpoView(context, appContext) {
    // Creates and initializes an event dispatcher for the `onLoad` event.
    // The name of the event is inferred from the value and needs to match the event name defined in the module.
    // private val onLoad by EventDispatcher()

    // Defines a WebView that will be used as the root subview.
    // internal val webView = WebView(context).apply {
    //     layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    //     webViewClient = object : WebViewClient() {
    //         override fun onPageFinished(view: WebView, url: String) {
    //             // Sends an event to JavaScript. Triggers a callback defined on the view component in JavaScript.
    //             onLoad(mapOf("url" to url))
    //         }
    //     }
    // }

    init {
        addView(ComposeView(context).apply {
            setContent {
                OnboardingViewContent()
            }
        })
    }
}

@Composable
private fun OnboardingViewContent() {
    Scaffold { contentPadding ->
        Box(Modifier.padding(contentPadding)) {
            Column {
                Text("Hello world!", style = TextStyle(fontSize = 40.sp))
                OnboardingComponents.DefaultTitleAndDescription(
                    title = "Hello",
                    description = "Description",
                    titleStyle = MaterialTheme.typography.bodyLarge,
                    descriptionStyle = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MaterialTheme {
        OnboardingViewContent()
    }
}
