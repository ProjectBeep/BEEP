package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "group_items_table",
    primaryKeys = ["group_id", "item_id"],
    foreignKeys = [
        ForeignKey(
            entity = DBGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DBItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["group_id"]),
        androidx.room.Index(value = ["item_id"])
    ]
)
internal data class DBGroupItemEntity(
    @ColumnInfo(name = "group_id") val groupId: String,
    @ColumnInfo(name = "item_id") val itemId: Long,
    @ColumnInfo(name = "shared_at") val sharedAt: Date,
    @ColumnInfo(name = "shared_by") val sharedBy: String
)