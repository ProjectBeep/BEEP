package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lighthouse.beep.model.location.Dms

@Entity(
    tableName = "brand_location_table",
    foreignKeys = [
        ForeignKey(
            entity = DBBrandSectionEntity::class,
            parentColumns = arrayOf("section_id"),
            childColumns = arrayOf("parent_section_id"),
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
internal data class DBBrandLocationEntity(
    @PrimaryKey
    @ColumnInfo(name = "place_url")
    val placeUrl: String,
    @ColumnInfo(name = "parent_section_id") val sectionId: Long,
    @ColumnInfo(name = "address_name") val addressName: String,
    @ColumnInfo(name = "place_name") val placeName: String,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "display_brand") val displayBrand: String,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "x") val x: Dms,
    @ColumnInfo(name = "y") val y: Dms,
)
