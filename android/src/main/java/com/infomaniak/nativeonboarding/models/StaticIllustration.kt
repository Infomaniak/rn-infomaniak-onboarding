package com.infomaniak.nativeonboarding.models

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class StaticIllustration(
    @Field val lightFileName: String,
    @Field val darkFileName: String,
) : Record {
    @get:Composable
    val fileName: String? get() = if (isSystemInDarkTheme()) darkFileName else lightFileName
}
