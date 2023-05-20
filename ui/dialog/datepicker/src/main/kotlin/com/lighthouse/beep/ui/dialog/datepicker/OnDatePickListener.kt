package com.lighthouse.beep.ui.dialog.datepicker

fun interface OnDatePickListener {
    fun onDatePick(year: Int, month: Int, date: Int)
}
