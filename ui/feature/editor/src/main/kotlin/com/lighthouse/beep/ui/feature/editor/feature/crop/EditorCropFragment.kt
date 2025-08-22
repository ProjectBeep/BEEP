package com.lighthouse.beep.ui.feature.editor.feature.crop

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.core.common.exts.EMPTY_RECT
import com.lighthouse.beep.core.common.exts.cast
import com.lighthouse.beep.core.common.exts.crop
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.designsystem.cropview.CropImageMode
import com.lighthouse.beep.ui.designsystem.cropview.OnChangeCropRectListener
import com.lighthouse.beep.ui.designsystem.cropview.OnCropImageModeListener
import com.lighthouse.beep.ui.feature.editor.EditorInfoProvider
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.FragmentEditorCropBinding
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.GifticonCropData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
internal class EditorCropFragment : Fragment(R.layout.fragment_editor_crop) {

    private val viewModel by viewModels<EditorCropViewModel>()

    private var _binding: FragmentEditorCropBinding? = null
    private val binding: FragmentEditorCropBinding
        get() = requireNotNull(_binding)

    private val requestManager: RequestManager by lazy {
        Glide.with(this)
    }

    private lateinit var cropInfoListener: EditorCropInfoListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        cropInfoListener = requireActivity().cast<EditorInfoProvider>().cropInfoListener
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
                val editType = cropInfoListener.selectedEditType ?: return
                job?.cancel()
                job = lifecycleScope.launch(Dispatchers.IO) {
                    val bitmap = originBitmap.crop(rect)
                    val editData = editType.createEditDataWithCrop(bitmap, rect, zoom)
                    cropInfoListener.updateGifticonData(editData)
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
            cropInfoListener.selectedGifticonFlow.flatMapLatest { gifticon ->
                    val bitmap = withContext(Dispatchers.IO) {
                        runCatching {
                            requestManager.asBitmap()
                                .load(gifticon.uri)
                                .submit()
                                .get()
                        }.getOrNull()
                    }
                    binding.cropGifticon.setBitmap(bitmap)

                cropInfoListener.selectedPropertyChipFlow.map { editorChip ->
                            val data = cropInfoListener.getGifticonData(gifticon.id)
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
                cropInfoListener.selectedPropertyChipFlow,
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
                cropInfoListener.selectedPropertyChipFlow,
                cropInfoListener.selectedGifticonDataFlow,
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
                    val type = cropInfoListener.selectedEditType
                    val data = cropInfoListener.selectedGifticonData
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