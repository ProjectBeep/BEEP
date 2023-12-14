package com.lighthouse.beep.ui.feature.login.page.login.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.lighthouse.beep.ui.feature.login.page.login.model.AppDescription
import com.lighthouse.beep.ui.feature.login.page.login.model.Diff

internal class AppDescriptionAdapter : ListAdapter<AppDescription, AppDescriptionViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppDescriptionViewHolder {
        return AppDescriptionViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AppDescriptionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}