package com.lighthouse.beep.data.local.database.model

import androidx.room.ColumnInfo
import java.util.Date

internal data class DBGifticonImageData(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "image_path") val imagePath: String,
    @ColumnInfo(name = "image_added_date") val imageAddedDate: Date,
)