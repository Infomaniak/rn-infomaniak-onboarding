package com.infomaniak.nativeonboarding

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

internal fun String.assetFileExists(context: Context): Boolean = context.resources.assets.list("")?.contains(this) == true

@Composable
internal fun String.assetFileExists(): Boolean = assetFileExists(LocalContext.current)
