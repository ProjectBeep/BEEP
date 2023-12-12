package com.lighthouse.beep.ui.dialog.datepicker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.ui.dialog.datepicker.databinding.ItemDatePickerBinding

internal class DatePickerViewHolder(
    parent: ViewGroup,
    private val binding: ItemDatePickerBinding = ItemDatePickerBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(value: Int) {
        binding.root.text = value.toString()
    }
}