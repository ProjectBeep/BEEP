package com.lighthouse.presentation.ui.addgifticon.adapter

import android.view.View
import com.lighthouse.presentation.model.AddGifticonUIModel

class AddCandidateGifticonDisplayModel(
    val item: AddGifticonUIModel.Gifticon,
    private val position: Int,
    private val onClick: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) {

    val closeVisibility = if (item.isDelete) View.VISIBLE else View.GONE
    val badgeVisibility = if (item.isDelete.not() && item.invalid) View.VISIBLE else View.GONE

    fun onClickItem() {
        if (item.isDelete) {
            onDelete(position)
        } else {
            onClick(position)
        }
    }
}
