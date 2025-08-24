package com.lighthouse.beep.data.local.database.model

import android.graphics.Rect
import android.net.Uri
import androidx.room.ColumnInfo
import java.util.Date

internal class DBGifticonDetail(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "code") val code: String,
    @ColumnInfo(name = "code_type") val codeType: String,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "display_brand") val displayBrand: String,
    @ColumnInfo(name = "expire_at") val expireAt: Date,
    @ColumnInfo(name = "is_cash_card") val isCashCard: Boolean,
    @ColumnInfo(name = "total_cash") val totalCash: Int?,
    @ColumnInfo(name = "remain_cash") val remainCash: Int?,
    @ColumnInfo(name = "is_used") val isUsed: Boolean,
    @ColumnInfo(name = "used_at") val usedAt: Date?,
    @ColumnInfo(name = "memo") val memo: String?,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "updated_at") val updatedAt: Date,
)
