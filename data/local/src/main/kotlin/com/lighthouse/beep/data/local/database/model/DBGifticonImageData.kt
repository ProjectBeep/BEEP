package com.lighthouse.beep.data.local.database.model

import androidx.room.ColumnInfo
import java.util.Date

internal data class DBGifticonImageData(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "display_brand") val displayBrand: String,
    @ColumnInfo(name = "origin_uri") val originUri: android.net.Uri?,
    @ColumnInfo(name = "cropped_rect") val croppedRect: android.graphics.Rect?,
    @ColumnInfo(name = "cropped_uri") val croppedUri: android.net.Uri?,
    @ColumnInfo(name = "thumbnail_uri") val thumbnailUri: android.net.Uri?,
)