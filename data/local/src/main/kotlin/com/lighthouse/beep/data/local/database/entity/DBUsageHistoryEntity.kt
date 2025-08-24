package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "usage_history_table",
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
internal data class DBUsageHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "firebase_id") val firebaseId: String?,
    @ColumnInfo(name = "item_id") val itemId: Long,
    @ColumnInfo(name = "item_type") val itemType: String,
    @ColumnInfo(name = "used_by") val usedBy: String,
    @ColumnInfo(name = "amount") val amount: Int?,
    @ColumnInfo(name = "group_id") val groupId: String?,
    @ColumnInfo(name = "used_at") val usedAt: Date,
    @ColumnInfo(name = "note") val note: String?
)