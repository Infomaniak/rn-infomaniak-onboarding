/*
 * Infomaniak kDrive - Android
 * Copyright (C) 2025 Infomaniak Network SA
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.infomaniak.nativeonboarding.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.infomaniak.core.compose.basics.Typography
import com.infomaniak.core.compose.basics.bottomsheet.BottomSheetThemeDefaults
import com.infomaniak.core.compose.basics.bottomsheet.LocalBottomSheetTheme
import com.infomaniak.nativeonboarding.models.OnboardingArgumentColors

val LocalCustomColors: ProvidableCompositionLocal<CustomColors> = staticCompositionLocalOf { CustomColors() }

@Composable
fun OnboardingTheme(
    colors: () -> OnboardingArgumentColors? = { null },
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable (() -> Unit),
) {
    val customColors = if (isDarkTheme) CustomDarkColors else CustomLightColors

    val bottomSheetTheme = BottomSheetThemeDefaults.theme(
        titleTextStyle = Typography.bodyMedium,
        titleColor = customColors.primaryTextColor,
    )

    CompositionLocalProvider(
        LocalBottomSheetTheme provides bottomSheetTheme,
        LocalCustomColors provides customColors,
    ) {
        MaterialTheme(colorScheme = getColorScheme(isDarkTheme, colors), content = content)
    }
}

private fun getColorScheme(isDarkTheme: Boolean, colors: () -> OnboardingArgumentColors?): ColorScheme {
    val colors = colors()
    return when {
        colors == null -> if (isDarkTheme) darkColorScheme() else lightColorScheme()
        isDarkTheme -> darkColorScheme(primary = colors.primaryColorDark, onPrimary = colors.onPrimaryColorDark)
        else -> lightColorScheme(primary = colors.primaryColorLight, onPrimary = colors.onPrimaryColorLight)
    }
}

object OnboardingTheme {
    val colors: CustomColors
        @Composable
        get() = LocalCustomColors.current
}

@Immutable
data class CustomColors(
    val primaryTextColor: Color = Color.Unspecified,
    val secondaryTextColor: Color = Color.Unspecified,
)
