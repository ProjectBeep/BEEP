package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail

internal sealed interface EditGifticonThumbnail {

    data class Default(
        val originUri: Uri,
    ): EditGifticonThumbnail

    data class Crop(
        val bitmap: Bitmap,
        val rect: Rect,
    ) : EditGifticonThumbnail

    data class BuiltIn(
        val builtIn: GifticonBuiltInThumbnail
    ) : EditGifticonThumbnail
}
