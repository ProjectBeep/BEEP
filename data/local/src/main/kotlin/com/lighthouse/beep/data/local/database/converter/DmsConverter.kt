package com.lighthouse.beep.data.local.database.converter

import androidx.room.TypeConverter
import com.lighthouse.beep.model.location.Dms

class DmsConverter {

    @TypeConverter
    fun decimalToDms(value: Double?): Dms {
        return value?.let { Dms(it) } ?: Dms.NULL
    }

    @TypeConverter
    fun dmsToDecimal(dms: Dms?): Double {
        return dms?.dd ?: 0.0
    }
}
