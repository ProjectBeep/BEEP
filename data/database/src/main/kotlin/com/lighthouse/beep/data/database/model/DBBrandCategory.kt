package com.lighthouse.beep.data.database.model

import androidx.room.ColumnInfo

internal data class DBBrandCategory(
    @ColumnInfo(name = "display_brand") val displayBrand: String,
    @ColumnInfo(name = "count") val count: Int,
)
