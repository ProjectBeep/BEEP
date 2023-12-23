package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail

internal sealed interface EditGifticonThumbnail {

    data class Default(
        val originUri: Uri,
    ): EditGifticonThumbnail {

        override fun load(requestManager: RequestManager): RequestBuilder<Drawable> {
            return requestManager.load(originUri)
                .transform(CenterCrop())
        }
    }

    data class Crop(
        val bitmap: Bitmap,
        val rect: Rect,
    ) : EditGifticonThumbnail {

        override fun load(requestManager: RequestManager): RequestBuilder<Drawable> {
            return requestManager.load(bitmap)
                .transition(DrawableTransitionOptions.withCrossFade())
        }
    }

    data class BuiltIn(
        val builtIn: GifticonBuiltInThumbnail
    ) : EditGifticonThumbnail {

        override fun load(requestManager: RequestManager): RequestBuilder<Drawable> {
            return requestManager.load(builtIn.smallIconRes)
        }
    }

    fun load(
        requestManager: RequestManager,
    ): RequestBuilder<Drawable>
}

internal fun RequestManager.loadThumbnail(thumbnail: EditGifticonThumbnail): RequestBuilder<Drawable> {
    return thumbnail.load(this)
}