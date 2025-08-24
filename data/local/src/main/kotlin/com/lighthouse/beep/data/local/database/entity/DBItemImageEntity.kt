package com.lighthouse.beep.data.local.database.entity

import android.graphics.Rect
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "item_images_table",
    foreignKeys = [
        ForeignKey(
            entity = DBItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["item_id"])
    ]
)
internal data class DBItemImageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "item_id") val itemId: Long,
    @ColumnInfo(name = "image_type") val imageType: String, // ORIGINAL, CROPPED, THUMBNAIL
    @ColumnInfo(name = "local_uri") val localUri: Uri?,
    @ColumnInfo(name = "firebase_url") val firebaseUrl: String?,
    @ColumnInfo(name = "cropped_rect") val croppedRect: Rect?,
    @ColumnInfo(name = "created_at") val createdAt: Date
)