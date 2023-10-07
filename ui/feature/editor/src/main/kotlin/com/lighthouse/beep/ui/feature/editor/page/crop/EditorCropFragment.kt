package com.lighthouse.beep.ui.feature.editor.page.crop

import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.core.graphics.toRect
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.lighthouse.beep.core.common.exts.EMPTY_RECT_F
import com.lighthouse.beep.core.common.exts.crop
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.designsystem.cropview.CropImageMode
import com.lighthouse.beep.ui.designsystem.cropview.OnChangeCropRectListener
import com.lighthouse.beep.ui.feature.editor.EditorViewModel
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.FragmentEditorCropBinding
import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class EditorCropFragment : Fragment(R.layout.fragment_editor_crop) {

    private val editorViewModel by activityViewModels<EditorViewModel>()

    private val binding by viewBindings<FragmentEditorCropBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpCropView()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpCropView() {
        binding.cropGifticon.setOnChangeCropRectListener(object: OnChangeCropRectListener {
            private var job: Job? = null

            override fun onChange(originBitmap: Bitmap, rect: RectF)  {
                val editType = editorViewModel.selectedEditType ?: return
                if (editType == EditType.THUMBNAIL) {
                    editorViewModel.updateGifticonData(editData= EditData.Thumbnail(rect))
                } else {
                    job?.cancel()
                    job = lifecycleScope.launch(Dispatchers.IO) {
                        val bitmap = originBitmap.crop(rect.toRect())
                        val editData = editType.createEditDataWithCrop(bitmap, rect)
                        editorViewModel.updateGifticonData(editData = editData)
                    }
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
//            val nextMode = when (currentMode) {
//                CropImageMode.DRAG_WINDOW -> CropImageMode.DRAW_PEN
//                CropImageMode.DRAW_PEN -> CropImageMode.DRAG_WINDOW
//            }
//            binding.iconCropTouchMode.setOnClickListener(createThrottleClickListener {
//                binding.cropGifticon.changeCropImageMode(nextMode)
//            })
//        }
    }

    private fun setUpCollectState() {
        viewLifecycleOwner.repeatOnStarted {
            editorViewModel.selectedGifticon
                .filterNotNull()
                .collect {
                    val request = ImageRequest.Builder(requireContext())
                        .lifecycle(viewLifecycleOwner)
                        .data(it.contentUri)
                        .size(Size.ORIGINAL)
                        .target { result ->
                            val data = editorViewModel.getGifticonData(it.id) ?: return@target
                            val type = editorViewModel.selectedEditType ?: EditType.THUMBNAIL
                            val bitmap = (result as? BitmapDrawable)?.bitmap ?: return@target
                            binding.cropGifticon.setBitmap(bitmap, type.getCropRectF(data))
                        }.build()
                    requireContext().imageLoader.enqueue(request)
                }
        }

        viewLifecycleOwner.repeatOnStarted {
            editorViewModel.selectedEditorChip.filterIsInstance<EditorChip.Property>().collect {
                when(it.type) {
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
            combine(
                editorViewModel.selectedGifticonDataFlow,
                editorViewModel.selectedEditorChip.filterIsInstance<EditorChip.Property>()
            ) { data, editorChip ->
                editorChip.type to editorChip.type.getCropRectF(data)
            }.collect { (type, rect) ->
                when(type != EditType.THUMBNAIL && rect == EMPTY_RECT_F) {
                    true -> binding.cropGifticon.selectCropImageTouchMode()
                    false -> binding.cropGifticon.selectCropImageWindowMode(rect)
                }

                val isChangeTouchModeVisible = type != EditType.THUMBNAIL && rect != EMPTY_RECT_F
                binding.groupCropTouchMode.isVisible = isChangeTouchModeVisible
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.iconCropTouchMode.setOnClickListener(createThrottleClickListener {
            when(binding.cropGifticon.cropImageMode) {
                CropImageMode.DRAG_WINDOW -> binding.cropGifticon.selectCropImageTouchMode()
                CropImageMode.DRAW_PEN -> {
                    val type = editorViewModel.selectedEditType ?: return@createThrottleClickListener
                    val data = editorViewModel.selectedGifticonData.value ?: return@createThrottleClickListener
                    val rect = type.getCropRectF(data)
                    if (rect != EMPTY_RECT_F) {
                        binding.cropGifticon.selectCropImageWindowMode(rect)
                    }
                }
            }
        })
    }
}