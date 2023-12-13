package com.lighthouse.beep.ui.dialog.datepicker

import android.os.Bundle
import androidx.lifecycle.SavedStateHandle
import com.lighthouse.beep.core.common.exts.EMPTY_DATE
import com.lighthouse.beep.core.common.exts.ofDate
import com.lighthouse.beep.core.common.exts.ofMonth
import com.lighthouse.beep.core.common.exts.ofYear
import java.util.Calendar
import java.util.Date

data class DatePickerParam(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
) {
    private constructor(calendar: Calendar) : this(
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH) + 1,
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
            val year = savedStateHandle.get<Int>(KEY_YEAR)
            if (year == null || year == EMPTY_DATE.ofYear()) {
                return Calendar.getInstance().get(Calendar.YEAR)
            }
            return year
        }

        fun getMonth(savedStateHandle: SavedStateHandle): Int {
            val month = savedStateHandle.get<Int>(KEY_MONTH)
            if (month == null || month == EMPTY_DATE.ofMonth()) {
                return Calendar.getInstance().get(Calendar.MONTH) + 1
            }
            return month
        }

        fun getDayOfMonth(savedStateHandle: SavedStateHandle): Int {
            val dayOfMonth = savedStateHandle.get<Int>(KEY_DAY_OF_MONTH)
            if (dayOfMonth == null || dayOfMonth == EMPTY_DATE.ofDate()) {
                return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            }
            return dayOfMonth
        }
    }
}
