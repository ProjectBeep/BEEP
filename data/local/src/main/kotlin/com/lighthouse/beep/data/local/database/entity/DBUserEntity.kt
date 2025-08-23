package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "users_table")
internal data class DBUserEntity(
    @PrimaryKey val id: String, // Firebase User ID
    @ColumnInfo(name = "display_name") val displayName: String,
    @ColumnInfo(name = "profile_image_url") val profileImageUrl: String?,
    @ColumnInfo(name = "last_updated") val lastUpdated: Date
)