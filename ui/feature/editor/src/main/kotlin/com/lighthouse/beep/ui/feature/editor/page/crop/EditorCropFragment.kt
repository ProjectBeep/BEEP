package com.lighthouse.beep.ui.feature.editor.page.crop

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.common.exts.EMPTY_RECT
import com.lighthouse.beep.core.common.exts.cast
import com.lighthouse.beep.core.common.exts.crop
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.designsystem.cropview.CropImageMode
import com.lighthouse.beep.ui.designsystem.cropview.OnChangeCropRectListener
import com.lighthouse.beep.ui.designsystem.cropview.OnCropImageModeListener
import com.lighthouse.beep.ui.feature.editor.EditorViewModel
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.FragmentEditorCropBinding
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.GifticonCropData
import com.lighthouse.beep.ui.feature.editor.provider.OnEditorProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
internal class EditorCropFragment : Fragment(R.layout.fragment_editor_crop) {

    private val editorViewModel by activityViewModels<EditorViewModel>()

    private val viewModel by viewModels<EditorCropViewModel>()

    private var _binding: FragmentEditorCropBinding? = null
    private val binding: FragmentEditorCropBinding
        get() = requireNotNull(_binding)

    private lateinit var requestManager: RequestManager

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requestManager = requireActivity().cast<OnEditorProvider>().requestManager
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditorCropBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpCropView()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpCropView() {
        binding.cropGifticon.setOnChangeCropRectListener(object : OnChangeCropRectListener {
            private var job: Job? = null

            override fun onChange(originBitmap: Bitmap, rect: Rect, zoom: Float) {
                val editType = editorViewModel.selectedEditType ?: return
                job?.cancel()
                job = lifecycleScope.launch(Dispatchers.IO) {
                    val bitmap = originBitmap.crop(rect)
                    val editData = editType.createEditDataWithCrop(bitmap, rect, zoom)
                    editorViewModel.updateGifticonData(editData = editData)
                }
            }
        })

        binding.cropGifticon.setOnCropImageModeListener(object: OnCropImageModeListener {
            override fun onChange(mode: CropImageMode) {
                viewModel.setCropImageMode(mode)
            }

            override fun onPenTouchStart() {
                viewModel.setShownCropImagePenGuide(true)
            }
        })
    }

    private fun setUpCollectState() {
        viewLifecycleOwner.repeatOnStarted {
            editorViewModel.selectedGifticon
                .filterNotNull()
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
                            editorChip.type to editorChip.type.getCropData(data)
                        }
                }.collect { (type, data) ->
                    when(type) {
                        EditType.THUMBNAIL -> {
                            binding.cropGifticon.enableAspectRatio = true
                            binding.cropGifticon.aspectRatio = 1f
                        }
                        else -> {
                            binding.cropGifticon.enableAspectRatio = false
                        }
                    }
                    binding.cropGifticon.setCropInfo(data.rect, data.zoom)
                }
        }

        viewLifecycleOwner.repeatOnStarted {
            combine(
                editorViewModel.selectedEditorChip.filterIsInstance<EditorChip.Property>(),
                viewModel.cropImageMode,
                viewModel.isShownCropImagePenGuide,
            ) { type, cropImageMode, isShowCropImagePendGuide ->
                Triple(type.type, cropImageMode, isShowCropImagePendGuide)
            }.collect { (type, cropImageMode, isShowCropImagePendGuide) ->
                val isShowGuide = cropImageMode == CropImageMode.DRAW_PEN && !isShowCropImagePendGuide
                val isShowTutorial = !isShowGuide && cropImageMode != CropImageMode.NONE

                binding.groupCropTouchModeGuide.isVisible = isShowGuide
                binding.groupCropTouchModeTutorial.isVisible = isShowTutorial

                if (isShowTutorial) {
                    val typeTitle = getString(type.textResId)
                    when(cropImageMode) {
                        CropImageMode.DRAW_PEN -> {
                            binding.textCropModeTutorial.text = getString(R.string.editor_crop_touch_mode_tutorial, typeTitle)
                            binding.iconCropModeTutorial.setImageResource(R.drawable.icon_crop_touch_mode_small)
                        }
                        CropImageMode.DRAG_WINDOW -> {
                            binding.textCropModeTutorial.text = getString(R.string.editor_crop_window_mode_tutorial, typeTitle)
                            binding.iconCropModeTutorial.setImageResource(R.drawable.icon_crop_window_mode_small)
                        }
                        else -> Unit
                    }
                }
            }
        }

        viewLifecycleOwner.repeatOnStarted {
            combine(
                editorViewModel.selectedEditorChip
                    .filterIsInstance<EditorChip.Property>(),
                editorViewModel.selectedGifticonDataFlow,
                viewModel.cropImageMode,
            ) { type, data, currentMode ->
                if (type.type == EditType.THUMBNAIL) {
                    return@combine CropImageMode.NONE
                }
                val cropData = type.type.getCropData(data)
                if (cropData.rect == EMPTY_RECT) {
                    return@combine CropImageMode.NONE
                }
                when (currentMode) {
                    CropImageMode.DRAW_PEN -> CropImageMode.DRAG_WINDOW
                    CropImageMode.DRAG_WINDOW -> CropImageMode.DRAW_PEN
                    CropImageMode.NONE -> CropImageMode.NONE
                }
            }.collect { cropImageMode ->
                binding.groupCropTouchMode.isVisible = cropImageMode != CropImageMode.NONE
                when (cropImageMode) {
                    CropImageMode.DRAG_WINDOW -> {
                        binding.textCropTouchMode.setText(R.string.editor_crop_window_mode)
                        binding.iconCropTouchMode.setImageResource(R.drawable.icon_crop_window_mode)
                    }

                    CropImageMode.DRAW_PEN -> {
                        binding.textCropTouchMode.setText(R.string.editor_crop_touch_mode)
                        binding.iconCropTouchMode.setImageResource(R.drawable.icon_crop_touch_mode)
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.iconCropTouchMode.setOnClickListener(createThrottleClickListener {
            when (viewModel.cropImageMode.value) {
                CropImageMode.DRAW_PEN -> {
                    val type = editorViewModel.selectedEditType
                    val data = editorViewModel.selectedGifticonData.value
                    val cropData = type?.getCropData(data) ?: GifticonCropData.None
                    binding.cropGifticon.setCropInfo(cropData.rect, cropData.zoom)
                }
                CropImageMode.DRAG_WINDOW -> {
                    binding.cropGifticon.setCropInfo(EMPTY_RECT, 0f)
                }
                CropImageMode.NONE -> Unit
            }
        })
    }
}