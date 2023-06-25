package com.lighthouse.beep.data.local.database.model

import androidx.room.ColumnInfo

internal class DBGifticonNotification(
    @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "d_day") val dDay: Int,
)
