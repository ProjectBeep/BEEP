package com.lighthouse.beep.ui.feature.editor.page.preview

import android.content.Context
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.size.Size
import com.lighthouse.beep.core.common.exts.cast
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.feature.editor.EditorViewModel
import com.lighthouse.beep.ui.feature.editor.OnEditorChipListener
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.FragmentEditorPreviewBinding
import com.lighthouse.beep.ui.feature.editor.model.EditType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class EditorPreviewFragment : Fragment(R.layout.fragment_editor_preview) {

    companion object {
        private val VIEW_RECT = RectF(0f, 0f, 96f.dp, 96f.dp)
    }

    private val previewModel by lazy {
        EditorPreviewModel(ViewModelProvider(requireActivity())[EditorViewModel::class.java])
    }

    private val binding by viewBindings<FragmentEditorPreviewBinding>()

    private lateinit var onEditorChipListener: OnEditorChipListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        onEditorChipListener = context.cast()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpPreview()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpPreview() {
        binding.imageThumbnail.clipToOutline = true
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            previewModel.thumbnailUri.collect { contentUri ->
                binding.imageThumbnail.load(contentUri) {
                    size(Size.ORIGINAL)
                }
            }
        }

        repeatOnStarted {
            previewModel.thumbnailCropData.collect { data ->
                if (data.isCropped) {
                    binding.imageThumbnail.scaleType = ImageView.ScaleType.MATRIX
                    binding.imageThumbnail.imageMatrix = data.calculateMatrix(VIEW_RECT)
                } else {
                    binding.imageThumbnail.scaleType = ImageView.ScaleType.CENTER_CROP
                }
            }
        }

        repeatOnStarted {
            previewModel.gifticonName.collect { name ->
                binding.textName.text = name
            }
        }

        repeatOnStarted {
            previewModel.brandName.collect { brand ->
                binding.textBrand.text = brand
            }
        }

        repeatOnStarted {
            previewModel.displayExpired.collect { expired ->
                binding.textExpired.text = expired
            }
        }

        repeatOnStarted {
            previewModel.displayBalance.collect { balance ->
                binding.textBalance.text = getString(R.string.editor_preview_balance, balance)
            }
        }

        repeatOnStarted {
            previewModel.barcodeImage.collect { image ->
                binding.imageBarcode.setImageBitmap(image)
            }
        }

        repeatOnStarted {
            previewModel.displayBarcode.collect { barcode ->
                binding.textBarcode.text = barcode
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.imageThumbnail.setOnClickListener(createThrottleClickListener {
            onEditorChipListener.selectEditorChip(EditType.THUMBNAIL)
        })

        binding.containerName.setOnClickListener(createThrottleClickListener {
            onEditorChipListener.selectEditorChip(EditType.NAME)
        })

        binding.containerBrand.setOnClickListener(createThrottleClickListener {
            onEditorChipListener.selectEditorChip(EditType.BRAND)
        })

        binding.containerExpired.setOnClickListener(createThrottleClickListener {
            onEditorChipListener.selectEditorChip(EditType.EXPIRED)
        })

        binding.containerBalance.setOnClickListener(createThrottleClickListener {
            onEditorChipListener.selectEditorChip(EditType.BALANCE)
        })

        binding.textBarcode.setOnClickListener(createThrottleClickListener {
            onEditorChipListener.selectEditorChip(EditType.BARCODE)
        })

        binding.imageBarcode.setOnClickListener(createThrottleClickListener {
            onEditorChipListener.selectEditorChip(EditType.BARCODE)
        })
    }
}