package com.lighthouse.beep.ui.designsystem.balloon

import android.content.Context
import android.graphics.Bitmap
import android.view.ViewGroup.LayoutParams
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import com.bumptech.glide.Glide

@Suppress("UNUSED")
class ImageBalloonBuilder(context: Context) : Balloon.Builder(context) {

    @Px
    var imageWidth: Int? = null

    @Px
    var imageHeight: Int? = null

    @DrawableRes
    var imageResource: Int? = null

    var imageUrl: String? = null

    var image: Bitmap? = null

    var balloonScaleType: ScaleType = ScaleType.CENTER_CROP

    fun setImageWidth(@Px value: Int) = apply {
        imageWidth = value
    }

    fun setImageHeight(@Px value: Int) = apply {
        imageHeight = value
    }

    fun setImageResource(@DrawableRes value: Int) = apply {
        imageResource = value
    }

    fun setImageUrl(value: String) = apply {
        imageUrl = value
    }

    fun setImage(value: Bitmap) = apply {
        image = value
    }

    fun setScaleType(scaleType: ScaleType) = apply {
        balloonScaleType = scaleType
    }

    override fun onPreBuild(dismissBalloon: () -> Unit) = apply {
        setIsVisibleArrow(false)
        setContentView(
            ImageView(context).apply {
                layoutParams = LayoutParams(
                    imageWidth ?: LayoutParams.WRAP_CONTENT,
                    imageHeight ?: LayoutParams.WRAP_CONTENT,
                )
                applyImage(this)
                scaleType = balloonScaleType
            }
        )
    }

    private fun applyImage(imageView: ImageView) {
        imageResource?.let {
            imageView.setImageResource(it)
            return
        }

        imageUrl?.let {
            Glide.with(context)
                .load(it)
                .into(imageView)
            return
        }

        imageView.setImageBitmap(image)
    }
}