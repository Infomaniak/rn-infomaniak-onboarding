package com.infomaniak.nativeonboarding.models

interface Page {
    val backgroundImage: StaticIllustration
    val staticIllustration: StaticIllustration?
    val animatedIllustration: AnimatedIllustration?
    val title: String
    val subtitle: String
}
