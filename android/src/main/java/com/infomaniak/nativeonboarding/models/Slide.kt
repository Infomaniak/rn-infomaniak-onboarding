package com.infomaniak.nativeonboarding.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class Slide(
    @Field override val backgroundImageNameLight: String,
    @Field override val backgroundImageNameDark: String,
    @Field override val staticIllustration: StaticIllustration?,
    @Field override val animatedIllustrationFileName: String?,
    @Field override val animatedIllustrationLightThemeName: String?,
    @Field override val animatedIllustrationDarkThemeName: String?,
    @Field override val title: String,
    @Field override val subtitle: String,
) : Record, Page
