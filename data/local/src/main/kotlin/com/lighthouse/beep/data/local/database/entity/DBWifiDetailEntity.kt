package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "wifi_details_table",
    foreignKeys = [
        ForeignKey(
            entity = DBItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class DBWifiDetailEntity(
    @PrimaryKey val item_id: Long,
    @ColumnInfo(name = "ssid") val ssid: String,
    @ColumnInfo(name = "password_encrypted") val passwordEncrypted: String,
    @ColumnInfo(name = "security_type") val securityType: String?,
    @ColumnInfo(name = "location_name") val locationName: String?
)