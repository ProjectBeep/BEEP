package com.lighthouse.data.database.model

import android.graphics.Rect
import android.net.Uri
import androidx.room.ColumnInfo
import java.util.Date

internal class DBGifticonWithCrop(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "has_image") val hasImage: Boolean,
    @ColumnInfo(name = "cropped_uri") val croppedUri: Uri?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "expire_at") val expireAt: Date,
    @ColumnInfo(name = "barcode") val barcode: String,
    @ColumnInfo(name = "is_cash_card") val isCashCard: Boolean,
    @ColumnInfo(name = "total_cash") val totalCash: Int,
    @ColumnInfo(name = "remain_cash") val remainCash: Int,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "cropped_rect") val croppedRect: Rect,
    @ColumnInfo(name = "is_used") val isUsed: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: Date
)
