package com.lighthouse.beep.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object BeepTextStyle {
    val TitleLarge = TextStyle(
        fontWeight = FontWeight(700),
        fontFamily = FontFamily(Font(R.font.pretendard_bold)),
        fontSize = 22.sp,
    )

    val TitleMedium = TextStyle(
        fontWeight = FontWeight(600),
        fontFamily = FontFamily(Font(R.font.pretendard_semi_bold)),
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
    )

    val TitleSmall = TextStyle(
        fontWeight = FontWeight(500),
        fontFamily = FontFamily(Font(R.font.pretendard_medium)),
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
    )

    val BodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 16.sp,
        letterSpacing = 0.15.sp,
    )

    val BodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
    )

    val BodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.pretendard_regular)),
        fontSize = 12.sp,
        letterSpacing = 0.4.sp,
    )
}
