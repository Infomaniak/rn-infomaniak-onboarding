// android/src/main/java/com/example/models/RNLoginConfiguration.kt
package com.infomaniak.nativeonboarding.models

import expo.modules.kotlin.records.Field
import expo.modules.kotlin.records.Record

data class LoginConfiguration(
    @Field var clientId: String,
    @Field private var loginURL: String?,
    @Field var redirectURIScheme: String,
    @Field var appVersionCode: Int,
    @Field var appVersionName: String,
    @Field var createAccountUrl: String,
    @Field("successHost") private var _successHost: String?,
    @Field("cancelHost") private var _cancelHost: String?,
) : Record {
    val loginUrl: String get() = loginURL ?: "https://login.infomaniak.com/"
    val successHost: String get() = _successHost ?: "ksuite.infomaniak.com"
    val cancelHost: String get() = _cancelHost ?: "welcome.infomaniak.com"
}
