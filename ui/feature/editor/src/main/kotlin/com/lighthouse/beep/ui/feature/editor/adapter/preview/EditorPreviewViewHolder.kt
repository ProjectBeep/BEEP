package com.lighthouse.beep.ui.feature.editor.adapter.preview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.library.barcode.BarcodeGenerator
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.ItemEditorPreviewBinding
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.loadThumbnail
import kotlinx.coroutines.flow.combine

internal class EditorPreviewViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnEditorPreviewListener,
    private val binding: ItemEditorPreviewBinding = ItemEditorPreviewBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ),
): LifecycleViewHolder<GalleryImage>(binding.root) {

    init {
        binding.imageThumbnail.clipToOutline = true
    }

    override fun onSetUpClickEvent(item: GalleryImage) {
        binding.switchCash.setOnCheckedChangeListener { _, isChecked ->
            listener.onCashChange(item, isChecked)
        }

        binding.imageThumbnail.setOnThrottleClickListener {
            listener.onEditClick(EditType.THUMBNAIL)
        }

        binding.textName.setOnThrottleClickListener {
            listener.onEditClick(EditType.NAME)
        }

        binding.iconNameEmpty.setOnThrottleClickListener {
            listener.onEditClick(EditType.NAME)
        }

        binding.textBrand.setOnThrottleClickListener {
            listener.onEditClick(EditType.BRAND)
        }

        binding.iconBrandEmpty.setOnThrottleClickListener {
            listener.onEditClick(EditType.BRAND)
        }

        binding.textExpired.setOnThrottleClickListener {
            listener.onEditClick(EditType.EXPIRED)
        }

        binding.iconExpiredEmpty.setOnThrottleClickListener {
            listener.onEditClick(EditType.EXPIRED)
        }

        binding.textBalance.setOnThrottleClickListener {
            listener.onEditClick(EditType.BALANCE)
        }

        binding.iconBalanceEmpty.setOnThrottleClickListener {
            listener.onEditClick(EditType.BALANCE)
        }

        binding.containerBarcodeEmpty.setOnThrottleClickListener {
            listener.onEditClick(EditType.BARCODE)
        }

        binding.imageBarcode.setOnThrottleClickListener {
            listener.onEditClick(EditType.BARCODE)
        }

        binding.textBarcode.setOnThrottleClickListener {
            listener.onEditClick(EditType.BARCODE)
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: GalleryImage) {
        val model = EditorPreviewDisplayModel(listener.getGifticonDataFlow(item))

        model.thumbnailCropData.collect(lifecycleOwner) { data ->
            requestManager.loadThumbnail(data)
                .into(binding.imageThumbnail)
        }

        model.gifticonName.collect(lifecycleOwner) { name ->
            val isEmpty = name.isEmpty()
            binding.iconNameEmpty.isVisible = isEmpty
            if (!isEmpty) {
                binding.textName.text = name
            }
        }

        model.brandName.collect(lifecycleOwner) { brand ->
            val isEmpty = brand.isEmpty()
            binding.iconBrandEmpty.isVisible = isEmpty
            if (!isEmpty) {
                binding.textBrand.text = brand
            }
        }

        model.displayExpired.collect(lifecycleOwner) { expired ->
            val isEmpty = expired.isEmpty()
            binding.iconExpiredEmpty.isVisible = isEmpty
            if (!isEmpty) {
                binding.textExpired.text = expired
            }
        }

        combine(
            model.isCash,
            model.displayBalance
        ) { isCash, balance ->
            isCash to balance
        }.collect(lifecycleOwner) { (isCash, balance) ->
            binding.switchCash.isChecked = isCash
            binding.textBalanceLabel.isVisible = isCash
            val isEmpty = balance.isEmpty()
            binding.textBalance.isVisible = isCash
            binding.iconBalanceEmpty.isVisible = isCash && isEmpty
            if (isCash && !isEmpty) {
                binding.textBalance.text = context.getString(R.string.editor_preview_balance, balance)
            }
        }

        model.displayBarcode.collect(
            lifecycleOwner = lifecycleOwner,
            defaultBlock = {
                binding.textBarcode.text = ""
                binding.imageBarcode.setImageBitmap(null)
            },
            block = { barcode ->
                binding.containerBarcodeEmpty.visibility = when(barcode.isEmpty()) {
                    true -> View.VISIBLE
                    false -> View.INVISIBLE
                }
                binding.groupBarcode.isVisible = barcode.isNotEmpty()
                if (barcode.isNotEmpty()) {
                    binding.textBarcode.text = barcode
                    val image = BarcodeGenerator.loadBarcode(barcode, 300.dp, 60.dp)
                    binding.imageBarcode.setImageBitmap(image)
                }
            }
        )
    }
}