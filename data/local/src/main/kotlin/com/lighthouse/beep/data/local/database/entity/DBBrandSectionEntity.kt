package com.lighthouse.beep.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lighthouse.beep.model.location.Dms

@Entity(tableName = "brand_section_table")
internal data class DBBrandSectionEntity(
    @PrimaryKey(autoGenerate = true) 
    val id: Long?,
    @ColumnInfo(name = "brand") 
    val brand: String,
    @ColumnInfo(name = "x") 
    val x: Dms,
    @ColumnInfo(name = "y") 
    val y: Dms
) {
    constructor(brand: String, x: Dms, y: Dms) : this(null, brand, x, y)
}