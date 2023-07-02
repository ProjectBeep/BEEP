package com.lighthouse.beep.ui.designsystem.dotindicator.type

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lighthouse.beep.ui.designsystem.dotindicator.Dot
import com.lighthouse.beep.ui.designsystem.dotindicator.DotShape
import kotlin.math.floor

class WormType(
    private val dotShape: DotShape = DotShape(color = Color.White),
    private val wormDotShape: DotShape = DotShape(color = Color.Black),
) : DotIndicatorType {

    @Composable
    override fun DotIndicator(
        offset: Float,
        modifier: Modifier,
        dotCount: Int,
        dotSpacing: Dp,
        onDotClick: (Int) -> Unit,
    ) {
        var firstDotPositionX by remember(dotCount) { mutableStateOf(-1f) }
        var lastDotPositionX by remember(dotCount) { mutableStateOf(-1f) }

        Box(modifier = modifier) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                content = {
                    items(dotCount) { index ->
                        val dotModifier = when (index) {
                            0 -> {
                                Modifier.onGloballyPositioned {
                                    firstDotPositionX = it.positionInParent().x
                                }
                            }

                            dotCount - 1 -> {
                                Modifier.onGloballyPositioned {
                                    lastDotPositionX = it.positionInParent().x
                                }
                            }

                            else -> Modifier
                        }
                        Dot(
                            dotShape,
                            dotModifier.clickable {
                                onDotClick.invoke(index)
                            },
                        )
                    }
                },
                horizontalArrangement = Arrangement.spacedBy(
                    space = dotSpacing,
                    alignment = Alignment.CenterHorizontally,
                ),
                contentPadding = PaddingValues(start = dotSpacing, end = dotSpacing),
            )
            if (firstDotPositionX != -1f && lastDotPositionX != -1f) {
                val centeredOffset by remember {
                    derivedStateOf {
                        (dotShape.size - wormDotShape.size) / 2
                    }
                }
                val density = LocalDensity.current.density
                val distanceBetweenDotsDp by remember {
                    derivedStateOf {
                        if (dotCount < 2) {
                            0.dp
                        } else {
                            ((lastDotPositionX - firstDotPositionX) / (dotCount - 1) / density).dp
                        }
                    }
                }
                val selectorDotWidthDp by remember(firstDotPositionX, lastDotPositionX) {
                    derivedStateOf {
                        distanceBetweenDotsDp + wormDotShape.size
                    }
                }
                val paddingStartDp by remember(offset) {
                    derivedStateOf {
                        val paddingOffset = ((offset % 1.0f - 0.5f) * 2f).coerceIn(0f, 1f)
                        distanceBetweenDotsDp * paddingOffset
                    }
                }
                val paddingEndDp by remember(offset) {
                    derivedStateOf {
                        val paddingOffset = 1f - ((offset % 1.0f) * 2f).coerceIn(0f, 1f)
                        distanceBetweenDotsDp * paddingOffset
                    }
                }
                val foregroundDotOffsetDp by remember(offset) {
                    derivedStateOf {
                        val foregroundDotPositionX = if (dotCount < 2) {
                            0f
                        } else {
                            firstDotPositionX + (lastDotPositionX - firstDotPositionX) / (dotCount - 1) * floor(
                                offset,
                            )
                        }
                        (foregroundDotPositionX / density).dp + centeredOffset
                    }
                }
                Dot(
                    dotShape = wormDotShape,
                    modifier = Modifier
                        .offset(
                            x = foregroundDotOffsetDp,
                            y = centeredOffset,
                        )
                        .width(selectorDotWidthDp)
                        .padding(start = paddingStartDp, end = paddingEndDp),
                )
            }
        }
    }
}
