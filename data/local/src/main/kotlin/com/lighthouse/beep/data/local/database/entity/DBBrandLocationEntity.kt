package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lighthouse.beep.model.location.Dms
import java.util.Date

@Entity(
    tableName = "brand_location_table",
    foreignKeys = [
        ForeignKey(
            entity = DBBrandSectionEntity::class,
            parentColumns = ["id"],
            childColumns = ["section_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        androidx.room.Index(value = ["section_id"])
    ]
)
internal data class DBBrandLocationEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Long?,
    @ColumnInfo(name = "section_id") 
    val sectionId: Long,
    @ColumnInfo(name = "place_url") 
    val placeUrl: String,
    @ColumnInfo(name = "place_name") 
    val placeName: String,
    @ColumnInfo(name = "address_name") 
    val addressName: String,
    @ColumnInfo(name = "category_name") 
    val categoryName: String,
    @ColumnInfo(name = "brand") 
    val brand: String,
    @ColumnInfo(name = "display_brand") 
    val displayBrand: String,
    @ColumnInfo(name = "x") 
    val x: Dms,
    @ColumnInfo(name = "y") 
    val y: Dms,
    @ColumnInfo(name = "created_at") 
    val createdAt: Date
)