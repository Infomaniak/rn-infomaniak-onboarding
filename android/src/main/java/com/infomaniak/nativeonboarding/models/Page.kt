package com.infomaniak.nativeonboarding.models

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

interface Page {
    val backgroundImage: StaticIllustration
    val staticIllustration: StaticIllustration?
    val animatedIllustration: AnimatedIllustration?
    val title: String
    val subtitle: String
}
