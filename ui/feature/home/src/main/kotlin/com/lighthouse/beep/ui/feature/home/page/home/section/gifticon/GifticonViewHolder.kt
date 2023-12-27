package com.lighthouse.beep.ui.feature.home.page.home.section.gifticon

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.exts.viewWidth
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.home.databinding.ItemExpiredGifticonBinding
import com.lighthouse.beep.ui.feature.home.model.GifticonViewMode
import com.lighthouse.beep.ui.feature.home.model.HomeItem
import com.lighthouse.beep.ui.feature.home.model.loadThumbnail

@SuppressLint("SetTextI18n")
internal class GifticonViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnGifticonListener,
    private val binding: ItemExpiredGifticonBinding = ItemExpiredGifticonBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : LifecycleViewHolder<HomeItem.GifticonItem>(binding.root) {

    init {
        binding.imageThumbnail.clipToOutline = true
    }

    override fun bind(item: HomeItem.GifticonItem) {
        super.bind(item)

        requestManager.loadThumbnail(item)
            .into(binding.imageThumbnail)

        binding.textBrand.text = item.brand
        binding.textGifticonName.text = item.name
        binding.textExpiredDate.text = item.formattedExpiredDate
        binding.textDday.text = "D-${item.dday}"

        val isExpired = item.dday < 0
        binding.containerExpired.isVisible = isExpired
        binding.textDday.isVisible = !isExpired
    }

    override fun onSetUpClickEvent(item: HomeItem.GifticonItem) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: HomeItem.GifticonItem) {
        listener.getNextDayEventFlow().collect(lifecycleOwner) { _ ->
            binding.textDday.text = "D-${item.dday}"
        }

        listener.isSelectedFlow(item).collect(
            lifecycleOwner = lifecycleOwner,
            defaultBlock = {
                binding.btnSelector.isSelected = false
            },
            block = { isSelected ->
                binding.btnSelector.isSelected = isSelected
            },
        )

        listener.getViewModeFlow().collect(lifecycleOwner) { mode, init ->
            val layoutParams =
                binding.containerGifticon.layoutParams as? MarginLayoutParams ?: return@collect
            val start = layoutParams.marginStart
            val end = when (mode) {
                GifticonViewMode.VIEW -> 0
                GifticonViewMode.EDIT -> binding.btnSelector.viewWidth
            }
            if (!init) {
                binding.containerGifticon.animate()
                    .setDuration(300)
                    .setInterpolator(DecelerateInterpolator())
                    .setUpdateListener {
                        binding.containerGifticon.updateLayoutParams<MarginLayoutParams> {
                            marginStart = (start - (start - end) * it.animatedFraction).toInt()
                        }
                    }.start()
            } else {
                binding.containerGifticon.updateLayoutParams<MarginLayoutParams> {
                    marginStart = end
                }
            }
        }
    }
}