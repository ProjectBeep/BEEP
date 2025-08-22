package com.lighthouse.beep.ui.dialog.datepicker.adapter

import androidx.recyclerview.widget.DiffUtil

internal class DatePickerDiff : DiffUtil.ItemCallback<Int>() {

    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }
}