package com.lighthouse.beep.ui.dialog.datepicker

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import java.util.Calendar
import java.util.Date

data class DatePickerParam(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
) {
    private constructor(calendar: Calendar) : this(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH),
    )

    constructor(date: Date) : this(
        Calendar.getInstance().apply { time = date },
    )

    fun buildBundle(): Bundle {
        return Bundle().apply {
            putInt(KEY_YEAR, year)
            putInt(KEY_MONTH, month)
            putInt(KEY_DAY_OF_MONTH, dayOfMonth)
        }
    }

    companion object {
        private const val KEY_YEAR = "Key.Year"
        private const val KEY_MONTH = "Key.Month"
        private const val KEY_DAY_OF_MONTH = "Key.DayOfMonth"

        fun getYear(savedStateHandle: SavedStateHandle): Int {
            return savedStateHandle.get<Int>(KEY_YEAR)
                ?: Calendar.getInstance().get(Calendar.YEAR)
        }

        fun getMonth(savedStateHandle: SavedStateHandle): Int {
            return savedStateHandle.get<Int>(KEY_MONTH)
                ?: Calendar.getInstance().get(Calendar.MONTH)
        }

        fun getDayOfMonth(savedStateHandle: SavedStateHandle): Int {
            return savedStateHandle.get<Int>(KEY_DAY_OF_MONTH)
                ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        }
    }
}
