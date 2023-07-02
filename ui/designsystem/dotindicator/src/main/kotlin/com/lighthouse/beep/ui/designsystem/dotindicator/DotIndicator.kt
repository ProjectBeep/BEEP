package com.lighthouse.beep.ui.designsystem.dotindicator

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lighthouse.beep.ui.designsystem.dotindicator.type.DotIndicatorType
import com.lighthouse.beep.ui.designsystem.dotindicator.type.WormType
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DotIndicator(
    dotCount: Int,
    modifier: Modifier = Modifier,
    dotSpacing: Dp = 8.dp,
    dotType: DotIndicatorType = WormType(),
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()
    DotIndicator(
        dotCount = dotCount,
        modifier = modifier,
        dotSpacing = dotSpacing,
        dotType = dotType,
        currentPage = pagerState.currentPage,
        currentPageOffsetFraction = pagerState.currentPageOffsetFraction,
        onDotClick = { index ->
            coroutineScope.launch {
                pagerState.animateScrollToPage(index)
            }
        },
    )
}

@Composable
internal fun DotIndicator(
    dotCount: Int,
    modifier: Modifier = Modifier,
    dotSpacing: Dp = 8.dp,
    dotType: DotIndicatorType = WormType(),
    currentPage: Int = 0,
    currentPageOffsetFraction: Float = 0.0f,
    onDotClick: (index: Int) -> Unit = {},
) {
    var scrollOffset by remember(dotCount, currentPage, currentPageOffsetFraction) {
        mutableStateOf(0f)
    }
    scrollOffset = computeScrollOffset(dotCount, currentPage, currentPageOffsetFraction)

    dotType.DotIndicator(
        offset = scrollOffset,
        modifier = modifier,
        dotCount = dotCount,
        dotSpacing = dotSpacing,
        onDotClick = onDotClick,
    )
}

private fun computeScrollOffset(totalCount: Int, position: Int, positionOffset: Float): Float {
    var offset = (position + positionOffset)
    val lastPageIndex = (totalCount - 1).toFloat()
    if (offset == lastPageIndex) {
        offset = lastPageIndex - 0.0001f
    }
    val leftPosition = offset.toInt()
    val rightPosition = leftPosition + 1
    if (rightPosition > lastPageIndex || leftPosition < 0) {
        return 0f
    }
    return leftPosition + offset % 1
}

@Composable
internal fun Dot(
    dotShape: DotShape,
    modifier: Modifier,
) {
    Box(
        modifier = modifier
            .background(
                color = dotShape.color,
                shape = dotShape.shape,
            )
            .size(dotShape.size),
    )
}

data class DotShape(
    val size: Dp = 8.dp,
    val color: Color = Color.White,
    val shape: Shape = CircleShape,
)

@Preview
@Composable
internal fun DotIndicatorPreview() {
    DotIndicator(
        dotCount = 10,
        currentPage = 1,
        currentPageOffsetFraction = 0.0f,
    )
}
