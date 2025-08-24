package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gallery_images_table")
internal data class DBGalleryImageDataEntity(
    @PrimaryKey
    @ColumnInfo(name = "image_path")
    val imagePath: String,
    @ColumnInfo(name = "date_added")
    val dateAdded: Long,
    @ColumnInfo(name = "is_gifticon")
    val isGifticon: Boolean
)