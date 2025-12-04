package com.infomaniak.nativeonboarding.models

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class StaticIllustration(
    @Field private val androidLightFileName: String,
    @Field private val androidDarkFileName: String,
) : Record {
    @get:Composable
    val fileName: String get() = if (isSystemInDarkTheme()) androidDarkFileName else androidLightFileName
}
