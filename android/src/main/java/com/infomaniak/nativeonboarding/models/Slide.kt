package com.infomaniak.nativeonboarding.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class Slide(
    @Field override val backgroundImageNameLight: String,
    @Field override val backgroundImageNameDark: String,
    @Field override val illustrationName: String,
    @Field override val illustrationLightThemeName: String?,
    @Field override val illustrationDarkThemeName: String?,
    @Field override val title: String,
    @Field override val subtitle: String,
) : Record, Page
