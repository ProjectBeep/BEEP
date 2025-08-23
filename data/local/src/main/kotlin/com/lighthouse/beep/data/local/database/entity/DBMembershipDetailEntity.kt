package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "membership_details_table",
    foreignKeys = [
        ForeignKey(
            entity = DBItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["item_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
internal data class DBMembershipDetailEntity(
    @PrimaryKey val item_id: Long,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "membership_number") val membershipNumber: String
)