package com.lighthouse.beep.data.local.database.model

import android.net.Uri
import androidx.room.ColumnInfo
import java.util.Date

internal class DBGifticonListItem(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "cropped_uri") val croppedUri: Uri?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "display_brand") val displayBrand: String,
    @ColumnInfo(name = "is_cash_card") val isCashCard: Boolean,
    @ColumnInfo(name = "total_cash") val totalCash: Int,
    @ColumnInfo(name = "remain_cash") val remainCash: Int,
    @ColumnInfo(name = "d_day") val dDay: Int,
    @ColumnInfo(name = "is_used") val isUsed: Boolean,
    @ColumnInfo(name = "expire_at") val expireAt: Date,
    @ColumnInfo(name = "created_at") val createdAt: Date,
)
