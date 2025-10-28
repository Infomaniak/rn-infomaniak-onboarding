// android/src/main/java/com/example/models/RNLoginConfiguration.kt
package com.infomaniak.nativeonboarding.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class LoginConfiguration(
    @Field var clientId: String,
    @Field var loginURL: String = "https://login.infomaniak.com/",
    @Field private var redirectURI: String? = null,
) : Record
