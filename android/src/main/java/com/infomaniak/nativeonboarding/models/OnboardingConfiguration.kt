package com.infomaniak.nativeonboarding.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class OnboardingConfiguration(
    @Field var primaryColorLight: String,
    @Field var primaryColorDark: String,
    @Field var onPrimaryColorLight: String,
    @Field var onPrimaryColorDark: String,
    @Field var slides: List<Slide>,
) : Record
