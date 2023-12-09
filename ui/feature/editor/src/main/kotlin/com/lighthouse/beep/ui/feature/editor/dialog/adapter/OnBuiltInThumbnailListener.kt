package com.lighthouse.beep.ui.feature.editor.dialog.adapter

import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail
import kotlinx.coroutines.flow.Flow

internal interface OnBuiltInThumbnailListener {

    fun isSelectedFlow(item: GifticonBuiltInThumbnail): Flow<Boolean>

    fun onClick(item: GifticonBuiltInThumbnail)
}