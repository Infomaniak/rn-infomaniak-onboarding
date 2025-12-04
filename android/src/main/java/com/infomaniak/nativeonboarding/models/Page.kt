package com.infomaniak.nativeonboarding.models

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

interface Page {
    val backgroundImageNameLight: String
    val backgroundImageNameDark: String
    val illustrationFileName: String
    val illustrationLightThemeName: String?
    val illustrationDarkThemeName: String?
    val title: String
    val subtitle: String

    @get:Composable
    val backgroundImageName: String get() = if (isSystemInDarkTheme()) backgroundImageNameDark else backgroundImageNameLight

    @get:Composable
    val illustrationThemeName: String? get() = if (isSystemInDarkTheme()) illustrationDarkThemeName else illustrationLightThemeName
}
