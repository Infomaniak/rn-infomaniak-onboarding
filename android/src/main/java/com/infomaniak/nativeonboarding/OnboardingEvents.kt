package com.infomaniak.nativeonboarding

import android.content.Context
import com.infomaniak.nativeonboarding.models.AnimatedIllustration
import com.infomaniak.nativeonboarding.models.StaticIllustration
import expo.modules.kotlin.viewevent.ViewEventCallback

private const val SUCCESS_EVENT_KEY = "accessTokens"
private const val ERROR_EVENT_KEY = "error"

interface OnboardingEvents {
    val onLoginSuccess: ViewEventCallback<Map<String, Any>>
    val onLoginError: ViewEventCallback<Map<String, Any>>

    fun reportAccessToken(accessToken: String) {
        onLoginSuccess(mapOf(SUCCESS_EVENT_KEY to listOf(accessToken)))
    }

    fun reportAccessTokens(accessTokens: List<String>) {
        if (accessTokens.isNotEmpty()) {
            onLoginSuccess(mapOf(SUCCESS_EVENT_KEY to accessTokens))
        }
    }

    fun reportMissingIllustration() {
        reportError("Missing illustration. Provide either a StaticIllustration or an AnimatedIllustration instance in each slide")
    }

    fun AnimatedIllustration.reportErrors(context: Context) {
        if (fileName.assetFileExists(context).not()) reportMissingAsset(fileName)
    }

    fun StaticIllustration.reportErrors(context: Context) {
        if (androidDarkFileName.assetFileExists(context).not()) reportMissingAsset(androidDarkFileName)
        if (androidLightFileName.assetFileExists(context).not()) reportMissingAsset(androidLightFileName)
    }

    private fun reportError(errorMessage: String) {
        onLoginError(mapOf(ERROR_EVENT_KEY to errorMessage))
    }

    private fun reportMissingAsset(fileName: String) {
        reportError("Missing file '$fileName' in Android assets folders")
    }
}
