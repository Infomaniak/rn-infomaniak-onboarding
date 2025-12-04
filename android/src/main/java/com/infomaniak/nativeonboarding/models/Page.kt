package com.infomaniak.nativeonboarding.models

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

interface Page {
    val backgroundImageNameLight: String
    val backgroundImageNameDark: String
    val staticIllustration: StaticIllustration?
    val animatedIllustrationFileName: String?
    val animatedIllustrationLightThemeName: String?
    val animatedIllustrationDarkThemeName: String?
    val title: String
    val subtitle: String

    @get:Composable
    val backgroundImageName: String get() = if (isSystemInDarkTheme()) backgroundImageNameDark else backgroundImageNameLight

    @get:Composable
    val illustrationThemeName: String? get() = if (isSystemInDarkTheme()) animatedIllustrationDarkThemeName else animatedIllustrationLightThemeName
}
