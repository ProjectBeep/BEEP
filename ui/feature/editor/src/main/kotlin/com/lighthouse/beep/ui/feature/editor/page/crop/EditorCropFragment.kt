package com.lighthouse.beep.ui.feature.editor.page.crop

import android.graphics.Bitmap
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.core.graphics.toRect
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import coil.imageLoader
import coil.request.ImageRequest
import coil.size.Size
import com.lighthouse.beep.core.common.exts.EMPTY_RECT_F
import com.lighthouse.beep.core.common.exts.crop
import com.lighthouse.beep.core.ui.binding.viewBindings
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
    }

    private fun setUpCropView() {
        binding.cropGifticon.setOnChangeCropRectListener(object: OnChangeCropRectListener {
            private var job: Job? = null

            override fun onChange(originBitmap: Bitmap, rect: RectF)  {
                val editType = editorViewModel.selectedEditType ?: return
                if (editType == EditType.THUMBNAIL) {
                    editorViewModel.updateGifticonData(EditData.Thumbnail(rect))
                } else {
                    job?.cancel()
                    job = lifecycleScope.launch(Dispatchers.IO) {
                        val bitmap = originBitmap.crop(rect.toRect())
                        editorViewModel.updateGifticonData(editType, bitmap, rect)
                    }
                }
            }
        })
    }

    private fun setUpCollectState() {
        repeatOnStarted {
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

        repeatOnStarted {
            combine(
                editorViewModel.selectedGifticon.filterNotNull(),
                editorViewModel.selectedEditorChip.filterIsInstance<EditorChip.Property>()
            ) { gifticon, editorChip ->
                val data = editorViewModel.getGifticonData(gifticon.id)
                editorChip.type to editorChip.type.getCropRectF(data)
            }.collect { (type, rect) ->
                when(type) {
                    EditType.THUMBNAIL -> {
                        binding.cropGifticon.enableAspectRatio = true
                        binding.cropGifticon.aspectRatio = 1f
                    }
                    else -> {
                        binding.cropGifticon.enableAspectRatio = false
                    }
                }

                binding.cropGifticon.cropImageMode = when {
                    type != EditType.THUMBNAIL && rect == EMPTY_RECT_F -> CropImageMode.DRAW_PEN
                    else -> CropImageMode.DRAG_WINDOW
                }

                binding.cropGifticon.setCropRect(rect)
            }
        }
    }
}