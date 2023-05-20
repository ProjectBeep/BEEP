package com.lighthouse.beep.ui.dialog.datepicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class DatePickerViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var year = DatePickerParams.getYear(savedStateHandle)
        set(value) {
            field = value
            _maxDayOfMonth.value = getMaxDayOfMonth(value, month)
        }

    var month = DatePickerParams.getMonth(savedStateHandle)
        set(value) {
            field = value
            _maxDayOfMonth.value = getMaxDayOfMonth(year, value)
        }

    var dayOfMonth = DatePickerParams.getDayOfMonth(savedStateHandle)

    private val _maxDayOfMonth = MutableStateFlow(getMaxDayOfMonth(year, month))
    val maxDayOfMonth = _maxDayOfMonth.asStateFlow()

    private fun getMaxDayOfMonth(year: Int, month: Int): Int {
        return if (month == 2 && year % 4 == 0 && year % 100 != 0) 29 else dayOfMonthPreset[month - 1]
    }

    companion object {
        val factory = viewModelFactory {
            initializer {
                DatePickerViewModel(createSavedStateHandle())
            }
        }

        private val dayOfMonthPreset = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        const val MIN_YEAR = 1900
        const val MAX_YEAR = 3000

        const val MIN_MONTH = 1
        const val MAX_MONTH = 12

        const val MIN_DAY_OF_MONTH = 1
        const val MAX_DAY_OF_MONTH = 31
    }
}
