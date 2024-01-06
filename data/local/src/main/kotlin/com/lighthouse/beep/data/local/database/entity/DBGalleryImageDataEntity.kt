package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "gallery_image_data")
internal data class DBGalleryImageDataEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "image_path")
    val imagePath: String,
    @ColumnInfo(name = "added_data")
    val addedDate: Date,
    @ColumnInfo(name = "is_gifticon")
    val isGifticon: Boolean,
    @ColumnInfo(name = "added_gifticon_id")
    val addedGifticonId: Long?,
)