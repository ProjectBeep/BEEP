package com.lighthouse.beep.ui.feature.home.page.home.section.gifticon

import android.annotation.SuppressLint
import android.content.res.ColorStateList
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
import com.lighthouse.beep.model.gifticon.GifticonThumbnail
import com.lighthouse.beep.theme.R as ThemeR
import com.lighthouse.beep.ui.feature.home.R
import com.lighthouse.beep.ui.feature.home.databinding.ItemExpiredGifticonBinding
import com.lighthouse.beep.ui.feature.home.model.GifticonViewMode
import com.lighthouse.beep.ui.feature.home.model.HomeItem

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
        binding.containerGifticon.clipToOutline = true
    }

    override fun bind(item: HomeItem.GifticonItem) {
        super.bind(item)

        when (val thumbnail = item.thumbnail) {
            is GifticonThumbnail.Image -> {
                requestManager
                    .load(thumbnail.uri)
                    .into(binding.imageThumbnail)
            }

            is GifticonThumbnail.BuildIn -> {
                requestManager
                    .load(thumbnail.icon.largeIconRes)
                    .into(binding.imageThumbnail)
            }
        }

        binding.textBrand.text = item.brand
        binding.textGifticonName.text = item.name
        binding.textExpiredDate.text = item.formattedExpiredDate

        binding.dividerBalance.isVisible = item.isCashCard
        binding.textBalance.isVisible = item.isCashCard

        binding.textBalance.text =
            context.getString(R.string.home_gifticon_balance_format, item.formattedBalance)

        updateGifticonByDDay(item)
    }

    private fun updateGifticonByDDay(item: HomeItem.GifticonItem) {
        val dday = item.dday

        val isExpired = dday < 0
        binding.textBrand.alpha = if (isExpired) 0.5f else 1f
        binding.textGifticonName.alpha = if (isExpired) 0.5f else 1f
        binding.textExpiredDate.alpha = if (isExpired) 0.5f else 1f
        binding.dividerBalance.alpha = if (isExpired) 0.5f else 1f
        binding.textBalance.alpha = if (isExpired) 0.5f else 1f

        binding.containerExpired.isVisible = isExpired
        binding.textDday.isVisible = !isExpired


        binding.textDday.text = when {
            dday < 0 -> ""
            dday == 0 -> "D-Day"
            else -> "D-${dday}"
        }

        when {
            dday == 0 -> {
                binding.textDday.backgroundTintList =
                    ColorStateList.valueOf(getColor(ThemeR.color.deep_gray))
                binding.textDday.setTextColor(getColor(ThemeR.color.white))
            }
            dday <= 7 -> {
                binding.textDday.backgroundTintList =
                    ColorStateList.valueOf(getColor(ThemeR.color.font_medium_gray))
                binding.textDday.setTextColor(getColor(ThemeR.color.white))
            }
            dday > 7 -> {
                binding.textDday.backgroundTintList =
                    ColorStateList.valueOf(getColor(ThemeR.color.light_gray))
                binding.textDday.setTextColor(getColor(ThemeR.color.font_medium_gray))
            }
        }
    }

    override fun onSetUpClickEvent(item: HomeItem.GifticonItem) {
        binding.root.setOnThrottleClickListener {
            listener.onClick(item)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: HomeItem.GifticonItem) {
        listener.getNextDayEventFlow().collect(lifecycleOwner) { _ ->
            updateGifticonByDDay(item)
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