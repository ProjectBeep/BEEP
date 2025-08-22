package com.lighthouse.beep.ui.dialog.datepicker.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter

internal class DatePickerAdapter : ListAdapter<Int, DatePickerViewHolder>(DatePickerDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatePickerViewHolder {
        return DatePickerViewHolder(parent)
    }

    override fun onBindViewHolder(holder: DatePickerViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getPosition(value: Int): Int {
        return currentList.indexOfFirst { it == value }
    }

    fun getValue(position: Int): Int? {
        return runCatching {
            currentList[position]
        }.getOrNull()
    }
}