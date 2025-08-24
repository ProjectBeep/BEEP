package com.lighthouse.beep.data.local.database.model

import android.net.Uri
import androidx.room.ColumnInfo

internal data class DBGifticonResource(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "display_brand") val displayBrand: String,
    @ColumnInfo(name = "origin_uri") val originUri: Uri?,
    @ColumnInfo(name = "cropped_rect") val croppedRect: android.graphics.Rect?,
    @ColumnInfo(name = "cropped_uri") val croppedUri: Uri?,
    @ColumnInfo(name = "thumbnail_uri") val thumbnailUri: Uri?,
)