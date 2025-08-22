package com.lighthouse.beep.ui.dialog.datepicker

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

internal class DatePickerViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    var year = DatePickerParam.getYear(savedStateHandle)
        private set

    val yearList = (year - 10 .. year + 10).toList()

    fun setYear(value: Int?) {
        value ?: return
        year = value
        maxDayOfMonth.value = getMaxDayOfMonth(value, month)
    }

    var month = DatePickerParam.getMonth(savedStateHandle)
        private set

    val monthList = (1 .. 12).toList()

    fun setMonth(value: Int?) {
        value ?: return
        month = value
        maxDayOfMonth.value = getMaxDayOfMonth(year, value)
    }

    var dayOfMonth = DatePickerParam.getDayOfMonth(savedStateHandle)
        private set

    fun setDayOfMonth(value: Int?) {
        value ?: return
        dayOfMonth = value
    }

    private val maxDayOfMonth = MutableStateFlow(getMaxDayOfMonth(year, month))

    val dateList = maxDayOfMonth.map { maxDayOfMonth ->
        (1 .. maxDayOfMonth).toList()
    }

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
    }
}
