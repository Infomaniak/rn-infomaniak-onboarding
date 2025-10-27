package com.infomaniak.nativeonboarding.images.illus

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.group
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infomaniak.nativeonboarding.images.AppImages.AppIllus

val AppIllus.OnboardingGradientLeft: ImageVector
    get() {
        if (_onboardingGradientLeft != null) {
            return _onboardingGradientLeft!!
        }
        _onboardingGradientLeft = Builder(
            name = "OnboardingGradientLeft",
            defaultWidth = 375.0.dp,
            defaultHeight = 302.0.dp,
            viewportWidth = 375.0f,
            viewportHeight = 302.0f,
        ).apply {
                group {
                    path(
                        fill = SolidColor(Color(0xFF0088B2)),
                        stroke = null,
                        strokeLineWidth = 0.0f,
                        strokeLineCap = Butt,
                        strokeLineJoin = Miter,
                        strokeLineMiter = 4.0f,
                        pathFillType = NonZero,
                    ) {
                        moveTo(173.0f, -158.5f)
                        arcToRelative(131.5f, 138.0f, 90.0f, true, false, -276.0f, -0.0f)
                        arcToRelative(131.5f, 138.0f, 90.0f, true, false, 276.0f, -0.0f)
                        close()
                    }
                }
            }.build()
        return _onboardingGradientLeft!!
    }

private var _onboardingGradientLeft: ImageVector? = null

@Preview
@Composable
private fun Preview() {
    Box(modifier = Modifier.padding(12.dp)) {
        Image(imageVector = AppIllus.OnboardingGradientLeft, contentDescription = null)
    }
}
