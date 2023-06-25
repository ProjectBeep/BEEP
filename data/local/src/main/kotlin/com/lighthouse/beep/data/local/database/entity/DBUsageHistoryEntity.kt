package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.lighthouse.beep.model.location.Dms
import java.util.Date

@Entity(
    tableName = "usage_history_table",
    primaryKeys = ["gifticon_id", "date"],
    foreignKeys = [
        ForeignKey(
            entity = DBGifticonEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("gifticon_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
internal data class DBUsageHistoryEntity(
    @ColumnInfo(name = "gifticon_id") val gifticonId: Long,
    @ColumnInfo(name = "date") val date: Date,
    @ColumnInfo(name = "x") val x: Dms,
    @ColumnInfo(name = "y") val y: Dms,
    @ColumnInfo(name = "amount") val amount: Int,
)
