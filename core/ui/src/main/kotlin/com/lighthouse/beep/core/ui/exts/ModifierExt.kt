package com.lighthouse.beep.core.ui.exts

import android.graphics.BlurMaskFilter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blur: Dp = 0.dp,
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            if (blur != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL))
            }
            frameworkPaint.color = color.toArgb()

            canvas.drawRect(
                left = offsetX.toPx(),
                top = offsetY.toPx(),
                right = offsetX.toPx() + size.width,
                bottom = offsetY.toPx() + size.height,
                paint = paint,
            )
        }
    },
)
