package com.lighthouse.beep.data.database.converter

import android.graphics.Rect
import androidx.room.TypeConverter

class RectConverter {

    @TypeConverter
    fun rectToString(rect: Rect?): String? {
        return rect?.let {
            "${it.left},${it.top},${it.right},${it.bottom}"
        }
    }

    @TypeConverter
    fun stringToRect(string: String?): Rect? {
        val data = string?.split(",") ?: return null
        if (data.size == 4 && data.all { it.toIntOrNull() != null }) {
            return Rect(data[0].toInt(), data[1].toInt(), data[2].toInt(), data[3].toInt())
        }
        return null
    }
}
