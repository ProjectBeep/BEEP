package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "groups_table")
internal data class DBGroupEntity(
    @PrimaryKey val id: String, // Firebase group ID
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "invite_code") val inviteCode: String,
    @ColumnInfo(name = "created_by") val createdBy: String,
    @ColumnInfo(name = "member_count") val memberCount: Int,
    @ColumnInfo(name = "joined_at") val joinedAt: Date,
    @ColumnInfo(name = "last_sync_at") val lastSyncAt: Date?
)