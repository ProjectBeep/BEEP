package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "gifticon_details_table",
    foreignKeys = [
        ForeignKey(
            entity = DBItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class DBGifticonDetailEntity(
    @PrimaryKey val item_id: Long,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "display_brand") val displayBrand: String,
    @ColumnInfo(name = "expire_at") val expireAt: Date,
    @ColumnInfo(name = "is_cash_card") val isCashCard: Boolean,
    @ColumnInfo(name = "total_cash") val totalCash: Int?,
    @ColumnInfo(name = "remain_cash") val remainCash: Int?,
    @ColumnInfo(name = "is_used") val isUsed: Boolean,
    @ColumnInfo(name = "used_at") val usedAt: Date?,
    @ColumnInfo(name = "memo") val memo: String?
)