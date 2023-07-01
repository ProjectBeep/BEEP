package com.lighthouse.beep.ui.page.intro.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val BeepIcon.Right: ImageVector
    get() {
        return _right ?: Builder(
            name = "ExpandRight",
            defaultWidth = 18.0.dp,
            defaultHeight =
            18.0.dp,
            viewportWidth = 18.0f,
            viewportHeight = 18.0f,
        ).apply {
            path(
                fill = SolidColor(Color(0x00000000)),
                stroke = SolidColor(Color(0xFFAEAAAE)),
                strokeLineWidth = 1.5f,
                strokeLineCap = Butt,
                strokeLineJoin = Miter,
                strokeLineMiter = 4.0f,
                pathFillType = NonZero,
            ) {
                moveTo(6.75f, 4.5f)
                lineTo(11.25f, 9.0f)
                lineTo(6.75f, 13.5f)
            }
        }.build().also {
            _right = it
        }
    }

private var _right: ImageVector? = null
