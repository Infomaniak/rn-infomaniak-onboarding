package com.infomaniak.nativeonboarding.preview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.infomaniak.nativeonboarding.models.Page
import com.infomaniak.nativeonboarding.models.StaticIllustration

class PagesPreviewParameter : PreviewParameterProvider<SnapshotStateList<Page>> {
    override val values: Sequence<SnapshotStateList<Page>> = sequenceOf(pages)
}

private val pages = mutableStateListOf<Page>(
    PreviewPage("Title 1"),
    PreviewPage("Title 2"),
    PreviewPage("Title 3"),
    PreviewPage("Title 4"),
)

private class PreviewPage(override val title: String) : Page {
    override val backgroundImageNameLight: String = ""
    override val backgroundImageNameDark: String = ""
    override val staticIllustration: StaticIllustration? = null
    override val animatedIllustrationFileName: String? = ""
    override val animatedIllustrationLightThemeName: String? = null
    override val animatedIllustrationDarkThemeName: String? = ""
    override val subtitle: String = "Hello World!"
}
