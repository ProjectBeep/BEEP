package com.lighthouse.beep.ui.designsystem.balloon

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.theme.R as ThemeR

@Suppress("UNUSED")
class TextBalloonBuilder(context: Context) : Balloon.Builder(context) {

    var balloonText: String = ""

    @StringRes
    var balloonTextRes: Int? = null

    @Px
    var balloonTextSize: Float = 13f.dp

    @ColorInt
    var balloonTextColor: Int = Color.BLACK

    @ColorRes
    var balloonTextColorRes: Int = ThemeR.color.white

    @StyleRes
    var balloonTextTextAppearanceRes: Int = 0

    var balloonTextMaxLine: Int? = 1

    var balloonEllipsize: TextUtils.TruncateAt? = TextUtils.TruncateAt.END

    var balloonTypeface: Typeface? = null

    var balloonTypefaceStyle: Int = Typeface.NORMAL

    var balloonUnderline: Boolean = false

    fun setText(value: String) = apply {
        balloonText = value
    }

    fun setText(resId: Int) = apply {
        balloonTextRes = resId
    }

    fun setTextSize(@Px value: Float) = apply {
        balloonTextSize = value
    }

    fun setTextColor(value: Int) = apply {
        balloonTextColor = value
    }

    fun setTextColorRes(@ColorRes resId: Int) = apply {
        balloonTextColorRes = resId
    }

    fun setTextAppearanceRes(@StyleRes resId: Int) = apply {
        balloonTextTextAppearanceRes = resId
    }

    fun setMaxLine(value: Int) = apply {
        balloonTextMaxLine = value
    }

    fun setEllipsize(value: TextUtils.TruncateAt) = apply {
        balloonEllipsize = value
    }

    fun setTypeface(value: Typeface?) = apply {
        balloonTypeface = value
    }

    fun setTypefaceStyle(value: Int) = apply {
        balloonTypefaceStyle = value
    }

    fun setUnderline(value: Boolean) = apply {
        balloonUnderline = value
    }

    override fun onPreBuild(dismissBalloon: () -> Unit) = apply {
        setContentView(
            TextView(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                gravity = Gravity.CENTER
                text = balloonTextRes?.let { context.getString(it) } ?: balloonText
                setTextSize(TypedValue.COMPLEX_UNIT_PX, balloonTextSize)
                val textColor = balloonTextColorRes.let { context.getColor(it) }
                setTextColor(textColor)
                setTypeface(balloonTypeface, balloonTypefaceStyle)
                if (balloonTextTextAppearanceRes != 0) {
                    setTextAppearance(balloonTextTextAppearanceRes)
                }
                if(balloonUnderline) {
                    paintFlags = Paint.UNDERLINE_TEXT_FLAG
                }
                balloonTextMaxLine?.let {
                    maxLines = it
                }
                balloonEllipsize?.let {
                    ellipsize = it
                }
            }
        )
    }
}