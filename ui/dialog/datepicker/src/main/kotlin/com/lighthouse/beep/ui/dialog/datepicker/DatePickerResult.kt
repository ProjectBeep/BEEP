package com.lighthouse.beep.ui.dialog.datepicker

import android.os.Bundle
import androidx.core.os.bundleOf
import com.lighthouse.beep.core.common.exts.EMPTY_DATE
import java.util.Calendar
import java.util.Date

data class DatePickerResult(
    val date: Date,
) {
    constructor(year: Int, month: Int, dayOfMonth: Int): this(
        Calendar.getInstance().apply {
            set(year, month - 1, dayOfMonth)
        }.time
    )

    constructor(bundle: Bundle?): this(getDate(bundle))

    companion object {
        const val KEY = "DatePickerResult"

        private const val KEY_DATE = "Key.Date"

        fun getDate(bundle: Bundle?): Date {
            return bundle?.getLong(KEY_DATE)?.let {
                Date(it)
            } ?: EMPTY_DATE
        }
    }

    fun buildBundle(): Bundle {
        return bundleOf(
            KEY_DATE to date.time
        )
    }
}