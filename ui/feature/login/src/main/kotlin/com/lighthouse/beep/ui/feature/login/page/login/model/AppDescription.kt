package com.lighthouse.beep.ui.feature.login.page.login.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.beep.ui.feature.login.R

internal enum class AppDescription(
    @StringRes val textRes: Int,
    @DrawableRes val imageRes: Int,
) {
    APP(
        textRes = R.string.app_description,
        imageRes = R.drawable.image_app,
    ),
    RECOGNIZE(
        textRes = R.string.recognize_description,
        imageRes = R.drawable.image_recognize,
    ),
    MAP(
        textRes = R.string.map_description,
        imageRes = R.drawable.image_map,
    );
}

internal val Diff = object: DiffUtil.ItemCallback<AppDescription>() {
    override fun areItemsTheSame(oldItem: AppDescription, newItem: AppDescription): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: AppDescription, newItem: AppDescription): Boolean {
        return oldItem == newItem
    }
}