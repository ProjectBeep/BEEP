package com.lighthouse.beep.ui.designsystem.dotindicator.type

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

interface DotIndicatorType {

    @Composable
    fun DotIndicator(
        offset: Float,
        modifier: Modifier,
        dotCount: Int,
        dotSpacing: Dp,
        onDotClick: (Int) -> Unit,
    )
}
