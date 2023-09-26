package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "gallery_recognize")
internal data class DBGalleryRecognizeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "image_path")
    val imagePath: String,
    @ColumnInfo(name = "date_added")
    val dateAdded: Date,
    @ColumnInfo(name = "is_gifticon")
    val isGifticon: Boolean
)