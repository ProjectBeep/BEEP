package com.lighthouse.beep.ui.feature.editor.list.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.LifecycleOwner
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.library.barcode.BarcodeGenerator
import com.lighthouse.beep.library.textformat.TextInputFormat
import com.lighthouse.beep.model.gifticon.GifticonBarcodeType
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.ItemEditorPreviewBinding
import com.lighthouse.beep.ui.feature.editor.model.EditGifticonThumbnail
import com.lighthouse.beep.ui.feature.editor.model.EditType
import kotlinx.coroutines.flow.combine

internal class EditorPreviewViewHolder(
    parent: ViewGroup,
    private val requestManager: RequestManager,
    private val listener: OnEditorPreviewListener,
    private val binding: ItemEditorPreviewBinding = ItemEditorPreviewBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    ),
) : LifecycleViewHolder<Long>(binding.root) {

    init {
        binding.imageThumbnail.clipToOutline = true

        binding.containerPreview.alpha = 0f
        binding.containerPreview.viewTreeObserver.addOnGlobalLayoutListener {
            val diff = binding.containerPreview.height - binding.scroll.height
            val newImageSize = maxOf(binding.imageThumbnail.height - diff, 120.dp)
            if (newImageSize != binding.imageThumbnail.height) {

                binding.imageThumbnail.updateLayoutParams {
                    width = newImageSize
                    height = newImageSize
                }
            }
        }
    }

    override fun bind(item: Long) {
        super.bind(item)
        binding.containerPreview.alpha = 0f
    }

    override fun onSetUpClickEvent(item: Long) {
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

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: Long) {
        val model = EditorPreviewDisplayModel(listener.getGifticonDataFlow(item))
        model.thumbnailCropData.collect(lifecycleOwner) { data ->
            when (data) {
                is EditGifticonThumbnail.Default -> {
                    requestManager
                        .load(data.thumbnailUri ?: data.originUri)
                        .transform(CenterCrop())
                        .into(binding.imageThumbnail)
                }

                is EditGifticonThumbnail.Crop -> {
                    requestManager
                        .load(data.bitmap)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.imageThumbnail)
                }

                is EditGifticonThumbnail.BuiltIn -> {
                    requestManager
                        .load(data.builtIn.largeIconRes)
                        .into(binding.imageThumbnail)
                }
            }
        }

        model.gifticonName.collect(lifecycleOwner) { name ->
            val isEmpty = name.isEmpty()
            binding.iconNameEmpty.isVisible = isEmpty
            binding.textName.text = name
        }

        model.brandName.collect(lifecycleOwner) { brand ->
            val isEmpty = brand.isEmpty()
            binding.iconBrandEmpty.isVisible = isEmpty
            binding.textBrand.text = brand
        }

        model.displayExpired.collect(lifecycleOwner) { expired ->
            val isEmpty = expired.isEmpty()
            binding.iconExpiredEmpty.isVisible = isEmpty
            binding.textExpired.text = expired
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
            if (isCash) {
                binding.textBalance.text = when (isEmpty) {
                    true -> ""
                    false -> context.getString(R.string.editor_preview_balance, balance)
                }
            }
        }

        var init = false
        combine(
            model.barcodeType,
            model.barcode,
        ) { type, barcode ->
            type to barcode
        }.collect(
            lifecycleOwner = lifecycleOwner,
            defaultBlock = {
                binding.imageBarcode.setImageBitmap(null)
            },
            block = { (type, barcode) ->
                binding.containerBarcodeEmpty.isVisible = barcode.isEmpty()
                binding.groupBarcode.isVisible = barcode.isNotEmpty()

                val textVisible = type != GifticonBarcodeType.QR_CODE && barcode.isNotEmpty()
                binding.textBarcode.isVisible = textVisible
                binding.textBarcode.text = TextInputFormat.BARCODE.valueToTransformed(barcode)

                binding.imageBarcode.updateLayoutParams {
                    when (type) {
                        GifticonBarcodeType.CODE_128 -> {
                            width = 300.dp
                            height = 82.dp
                        }
                        GifticonBarcodeType.QR_CODE -> {
                            width = 120.dp
                            height = 120.dp
                        }
                    }
                }

                if (barcode.isNotEmpty()) {
                    val image = when(type) {
                        GifticonBarcodeType.CODE_128 -> BarcodeGenerator.loadBarcode(barcode, 300.dp, 82.dp)
                        GifticonBarcodeType.QR_CODE -> BarcodeGenerator.loadQrCode(barcode, 120.dp, 120.dp)
                    }
                    binding.imageBarcode.setImageBitmap(image)
                }

                if (!init) {
                    init = true
                    binding.containerPreview.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .start()
                }
            }
        )
    }
}