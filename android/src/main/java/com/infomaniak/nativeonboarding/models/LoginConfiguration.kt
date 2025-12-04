// android/src/main/java/com/example/models/RNLoginConfiguration.kt
package com.infomaniak.nativeonboarding.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class LoginConfiguration(
    @Field var clientId: String,
    @Field private var loginURL: String? = null,
    @Field var redirectURIScheme: String,
    @Field var appVersionCode: Int,
    @Field var appVersionName: String,
) : Record {
    val loginUrl: String get() = loginURL ?: "https://login.infomaniak.com/"
}
