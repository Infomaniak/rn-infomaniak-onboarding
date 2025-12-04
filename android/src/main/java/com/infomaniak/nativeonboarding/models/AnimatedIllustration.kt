package com.infomaniak.nativeonboarding.models

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class AnimatedIllustration(
    @Field val fileName: String,
    @Field val lightThemeName: String?,
    @Field val darkThemeName: String?,
) : Record {
    @get:Composable
    val themeName: String? get() = if (isSystemInDarkTheme()) darkThemeName else lightThemeName
}
