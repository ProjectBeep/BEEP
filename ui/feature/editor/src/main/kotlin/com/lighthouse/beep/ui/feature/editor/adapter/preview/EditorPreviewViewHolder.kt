package com.lighthouse.beep.ui.feature.editor.adapter.preview

import android.graphics.RectF
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import coil.load
import coil.size.Size
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.ItemEditorPreviewBinding
import com.lighthouse.beep.ui.feature.editor.model.EditType

internal class EditorPreviewViewHolder(
    parent: ViewGroup,
    private val listener: OnEditorPreviewListener,
    private val binding: ItemEditorPreviewBinding = ItemEditorPreviewBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ),
): LifecycleViewHolder<GalleryImage>(binding.root) {

    companion object {
        private val VIEW_RECT = RectF(0f, 0f, 96f.dp, 96f.dp)
    }

    init {
        binding.imageThumbnail.clipToOutline = true
    }

    override fun bind(item: GalleryImage) {
        super.bind(item)

        binding.imageThumbnail.load(item.contentUri) {
            size(Size.ORIGINAL)
        }
    }

    override fun onSetUpClickEvent(item: GalleryImage) {
        binding.switchCash.setOnCheckedChangeListener { _, isChecked ->
            listener.onCashChange(item, isChecked)
        }

        binding.imageThumbnail.setOnThrottleClickListener {
            listener.onEditClick(EditType.THUMBNAIL)
        }

        binding.containerName.setOnThrottleClickListener {
            listener.onEditClick(EditType.NAME)
        }

        binding.containerBrand.setOnThrottleClickListener {
            listener.onEditClick(EditType.BRAND)
        }

        binding.containerExpired.setOnThrottleClickListener {
            listener.onEditClick(EditType.EXPIRED)
        }

        binding.containerBalance.setOnThrottleClickListener {
            listener.onEditClick(EditType.BALANCE)
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
            if (data.isCropped) {
                binding.imageThumbnail.scaleType = ImageView.ScaleType.MATRIX
                binding.imageThumbnail.imageMatrix = data.calculateMatrix(VIEW_RECT)
            } else {
                binding.imageThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
            }
        }

        model.gifticonName.collect(lifecycleOwner) { name ->
            binding.textName.text = name
        }

        model.brandName.collect(lifecycleOwner) { brand ->
            binding.textBrand.text = brand
        }

        model.displayExpired.collect(lifecycleOwner) { expired ->
            binding.textExpired.text = expired
        }

        model.isCash.collect(lifecycleOwner) { isCash ->
            binding.switchCash.isChecked = isCash
            binding.containerBalance.isVisible = isCash
        }

        model.displayBalance.collect(lifecycleOwner) { balance ->
            binding.textBalance.text = context.getString(R.string.editor_preview_balance, balance)
        }

        model.barcodeImage.collect(lifecycleOwner) { image ->
            binding.imageBarcode.setImageBitmap(image)
        }

        model.displayBarcode.collect(lifecycleOwner) { barcode ->
            binding.textBarcode.text = barcode
        }
    }
}