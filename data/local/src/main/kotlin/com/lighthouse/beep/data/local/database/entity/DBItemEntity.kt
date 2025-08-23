package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "items_table")
internal data class DBItemEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long?,
    @ColumnInfo(name = "firebase_id") val firebaseId: String?,
    @ColumnInfo(name = "type") val type: String, // GIFTICON, WIFI, MEMBERSHIP
    @ColumnInfo(name = "owner_id") val ownerId: String,
    @ColumnInfo(name = "code") val code: String, // 바코드/QR코드 데이터
    @ColumnInfo(name = "code_type") val codeType: String, // QR_CODE, CODE_128 등
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "updated_at") val updatedAt: Date,
    @ColumnInfo(name = "sync_status") val syncStatus: String // SYNCED, PENDING, CONFLICT
)