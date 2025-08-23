package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "pending_changes_table")
internal data class DBPendingChangeEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "item_id") val itemId: Long,
    @ColumnInfo(name = "change_type") val changeType: String, // CREATED, MODIFIED, DELETED
    @ColumnInfo(name = "timestamp") val timestamp: Date
)