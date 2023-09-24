package com.lighthouse.beep.ui.feature.home.adapter.expired

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.home.databinding.ItemExpiredGifticonBinding
import com.lighthouse.beep.ui.feature.home.model.ExpiredGifticonItem

@SuppressLint("SetTextI18n")
internal class ExpiredGifticonViewHolder(
    parent: ViewGroup,
    private val listener: OnExpiredGifticonListener,
    private val binding: ItemExpiredGifticonBinding = ItemExpiredGifticonBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<ExpiredGifticonItem>(binding.root) {

    override fun bind(item: ExpiredGifticonItem) {
        super.bind(item)

        binding.textBrand.text = item.brand
        binding.textGifticonName.text = item.name
        binding.textExpired.text = item.formattedExpiredDate
        binding.textDday.text = "D-${item.dday}"
    }

    override fun onSetUpClickEvent(item: ExpiredGifticonItem) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: ExpiredGifticonItem) {
        listener.getNextDayEventFlow().collect(lifecycleOwner) { _ ->
            binding.textDday.text = "D-${item.dday}"
        }
    }
}