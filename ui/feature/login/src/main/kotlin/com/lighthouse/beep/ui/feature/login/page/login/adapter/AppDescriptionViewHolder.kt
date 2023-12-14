package com.lighthouse.beep.ui.feature.login.page.login.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.ui.feature.login.databinding.ItemAppDescriptionBinding
import com.lighthouse.beep.ui.feature.login.page.login.model.AppDescription

internal class AppDescriptionViewHolder(
    parent: ViewGroup,
    private val binding: ItemAppDescriptionBinding = ItemAppDescriptionBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ),
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AppDescription) {
        binding.textAppDescription.setText(item.textRes)
        binding.imageAppDescription.setImageResource(item.imageRes)
    }
}