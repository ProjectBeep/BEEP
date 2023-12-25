package com.lighthouse.beep.ui.feature.home.page.home.section.gifticon

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LifecycleOwner
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
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
        binding.textExpired.text = item.formattedExpiredDate
        binding.textDday.text = "D-${item.dday}"
    }

    override fun onSetUpClickEvent(item: HomeItem.GifticonItem) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }

    private val gifticonViewModeSet: ConstraintSet
        get() = ConstraintSet().apply {
            clone(binding.root)
            connect(
                binding.containerGifticon.id,
                ConstraintSet.START,
                binding.root.id,
                ConstraintSet.START
            )
        }

    private val gifticonEditModeSet: ConstraintSet
        get() = ConstraintSet().apply {
            clone(binding.root)
            connect(
                binding.containerGifticon.id,
                ConstraintSet.START,
                binding.root.id,
                ConstraintSet.END
            )
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

        listener.getViewModeFlow().collect(lifecycleOwner){ mode, init ->
            Log.d("TEST", "mode : $mode")
            val set = when (mode) {
                GifticonViewMode.VIEW -> gifticonViewModeSet
                GifticonViewMode.EDIT -> gifticonEditModeSet
            }
            set.applyTo(binding.root)
            val trans = ChangeBounds().apply {
                duration = 300L
                interpolator = DecelerateInterpolator()
            }
            TransitionManager.beginDelayedTransition(binding.root, trans)
        }
    }
}