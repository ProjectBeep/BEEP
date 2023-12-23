package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lighthouse.beep.model.location.Dms
import java.util.Date

@Entity(
    tableName = "brand_section_table",
)
internal data class DBBrandSectionEntity(
    // TODO : "0000000_0000000_brand" String 으로 변경
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "section_id")
    val id: Long?,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "x") val x: Dms,
    @ColumnInfo(name = "y") val y: Dms,
    @ColumnInfo(name = "search_date") val searchDate: Date = Date(),
) {
    constructor(brand: String, x: Dms, y: Dms, searchDate: Date = Date()) : this(
        null,
        brand,
        x,
        y,
        searchDate,
    )
}
