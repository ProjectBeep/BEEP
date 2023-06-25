package com.lighthouse.beep.data.local.database.entity

import android.graphics.Rect
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "gifticon_table")
internal data class DBGifticonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long?,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "cropped_uri") val croppedUri: Uri?,
    @ColumnInfo(name = "cropped_rect") val croppedRect: Rect,
    @ColumnInfo(name = "origin_uri") val originUri: Uri?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "display_brand") val displayBrand: String,
    @ColumnInfo(name = "barcode") val barcode: String,
    @ColumnInfo(name = "is_cash_card") val isCashCard: Boolean,
    @ColumnInfo(name = "total_cash") val totalCash: Int,
    @ColumnInfo(name = "remain_cash") val remainCash: Int,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "is_used") val isUsed: Boolean,
    @ColumnInfo(name = "expire_at") val expireAt: Date,
    @ColumnInfo(name = "updated_at") val updatedAt: Date,
    @ColumnInfo(name = "created_at") val createdAt: Date,
)
