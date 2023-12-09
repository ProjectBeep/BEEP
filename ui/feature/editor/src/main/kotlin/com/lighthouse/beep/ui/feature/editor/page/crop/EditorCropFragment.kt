package com.lighthouse.beep.ui.feature.editor.page.crop

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import androidx.core.graphics.toRect
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.common.exts.cast
import com.lighthouse.beep.core.common.exts.crop
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.designsystem.cropview.CropImageMode
import com.lighthouse.beep.ui.designsystem.cropview.OnChangeCropRectListener
import com.lighthouse.beep.ui.feature.editor.EditorViewModel
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.FragmentEditorCropBinding
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.provider.OnEditorProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
internal class EditorCropFragment : Fragment(R.layout.fragment_editor_crop) {

    private val editorViewModel by activityViewModels<EditorViewModel>()

    private val binding by viewBindings<FragmentEditorCropBinding>()

    private lateinit var requestManager: RequestManager

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requestManager = requireActivity().cast<OnEditorProvider>().requestManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpCropView()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpCropView() {
        binding.cropGifticon.setOnChangeCropRectListener(object : OnChangeCropRectListener {
            private var job: Job? = null

            override fun onChange(originBitmap: Bitmap, rect: RectF, zoom: Float) {
                val editType = editorViewModel.selectedEditType ?: return
                job?.cancel()
                job = lifecycleScope.launch(Dispatchers.IO) {
                    val bitmap = originBitmap.crop(rect.toRect())
                    val editData = editType.createEditDataWithCrop(bitmap, rect)
                    editorViewModel.updateGifticonData(editData = editData)
                }
            }
        })

//        binding.cropGifticon.setOnCropImageModeChangeListener { currentMode ->
//            when (currentMode) {
//                CropImageMode.DRAG_WINDOW -> {
//                    binding.textCropTouchMode.setText(R.string.editor_crop_touch_mode)
//                    binding.iconCropTouchMode.setImageResource(R.drawable.icon_crop_touch_mode)
//                }
//                CropImageMode.DRAW_PEN -> {
//                    binding.textCropTouchMode.setText(R.string.editor_crop_window_mode)
//                    binding.iconCropTouchMode.setImageResource(R.drawable.icon_crop_window_mode)
//                }
//            }
//        }
    }

    private fun setUpCollectState() {
        viewLifecycleOwner.repeatOnStarted {
            editorViewModel.selectedEditorChip.filterIsInstance<EditorChip.Property>().collect {
                when (it.type) {
                    EditType.THUMBNAIL -> {
                        binding.cropGifticon.enableAspectRatio = true
                        binding.cropGifticon.aspectRatio = 1f
                    }

                    else -> {
                        binding.cropGifticon.enableAspectRatio = false
                    }
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            editorViewModel.cropImageMode.collect { cropImageMode ->
                when (cropImageMode) {
                    CropImageMode.DRAG_WINDOW -> {
                        binding.textCropTouchMode.setText(R.string.editor_crop_touch_mode)
                        binding.iconCropTouchMode.setImageResource(R.drawable.icon_crop_touch_mode)
                    }

                    CropImageMode.DRAW_PEN -> {
                        binding.textCropTouchMode.setText(R.string.editor_crop_window_mode)
                        binding.iconCropTouchMode.setImageResource(R.drawable.icon_crop_window_mode)
                    }
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            editorViewModel.selectedGifticon
                .filterNotNull()
                .distinctUntilChanged()
                .flatMapLatest { gifticon ->
                    val bitmap = withContext(Dispatchers.IO) {
                        requestManager.asBitmap()
                            .load(gifticon.contentUri)
                            .submit()
                            .get()
                    }
                    binding.cropGifticon.setBitmap(bitmap)
                    editorViewModel.selectedEditorChip
                        .filterIsInstance<EditorChip.Property>()
                        .map { editorChip ->
                            val data = editorViewModel.getGifticonData(gifticon.id)
                            editorChip.type to editorChip.type.getCropRectF(data)
                        }
                }.collect { (type, rect) ->

//                    when (type != EditType.THUMBNAIL && rect == EMPTY_RECT_F) {
//                        true -> binding.cropGifticon.selectCropImageTouchMode()
//                        false -> binding.cropGifticon.selectCropImageWindowMode(rect)
//                    }
//
//                    val isChangeTouchModeVisible =
//                        type != EditType.THUMBNAIL && rect != EMPTY_RECT_F
//                    binding.groupCropTouchMode.isVisible = isChangeTouchModeVisible
                }

//            combine(
//                editorViewModel.selectedGifticon.filterNotNull(),
//                editorViewModel.selectedEditorChip.filterIsInstance<EditorChip.Property>()
//            ) { gifticon, editorChip ->
//                val data = editorViewModel.getGifticonData(gifticon.id)
//                editorChip.type to editorChip.type.getCropRectF(data)
//            }.collect { (type, rect) ->
//                when(type != EditType.THUMBNAIL && rect == EMPTY_RECT_F) {
//                    true -> binding.cropGifticon.selectCropImageTouchMode()
//                    false -> binding.cropGifticon.selectCropImageWindowMode(rect)
//                }
//
//                val isChangeTouchModeVisible = type != EditType.THUMBNAIL && rect != EMPTY_RECT_F
//                binding.groupCropTouchMode.isVisible = isChangeTouchModeVisible
//            }
        }
    }

    private fun setUpOnClickEvent() {
//        binding.iconCropTouchMode.setOnClickListener(createThrottleClickListener {
//            val cropImageMode = when (editorViewModel.cropImageMode.value) {
//                CropImageMode.DRAW_PEN -> CropImageMode.DRAG_WINDOW
//                CropImageMode.DRAG_WINDOW -> CropImageMode.DRAW_PEN
//            }
//            editorViewModel.setCropImageMode(cropImageMode)

//            when(binding.cropGifticon.cropImageMode) {
//                CropImageMode.DRAG_WINDOW -> binding.cropGifticon.selectCropImageTouchMode()
//                CropImageMode.DRAW_PEN -> {
//                    val type = editorViewModel.selectedEditType ?: return@createThrottleClickListener
//                    val data = editorViewModel.selectedGifticonData.value ?: return@createThrottleClickListener
//                    val rect = type.getCropRectF(data)
//                    if (rect != EMPTY_RECT_F) {
//                        binding.cropGifticon.selectCropImageWindowMode(rect)
//                    }
//                }
//            }
//        })
    }
}