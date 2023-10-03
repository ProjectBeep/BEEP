package com.lighthouse.beep.ui.feature.editor.page.crop

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import coil.imageLoader
import coil.request.ImageRequest
import com.lighthouse.beep.core.common.exts.EMPTY_RECT_F
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.designsystem.cropview.CropImageMode
import com.lighthouse.beep.ui.feature.editor.EditorViewModel
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.FragmentEditorCropBinding
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
internal class EditorCropFragment : Fragment(R.layout.fragment_editor_crop) {

    private val editorViewModel by activityViewModels<EditorViewModel>()

    private val viewModel by viewModels<EditorCropViewModel>()

    private val binding by viewBindings<FragmentEditorCropBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpCropView()
        setUpCollectState()
    }

    private fun setUpCropView() {

    }

    private fun setUpCollectState() {
        repeatOnStarted {
            editorViewModel.selectedGifticon
                .map { it?.contentUri }
                .filterNotNull()
                .collect { uri ->
                    val request = ImageRequest.Builder(requireContext())
                        .lifecycle(viewLifecycleOwner)
                        .data(uri)
                        .target { result ->
                            val bitmap = (result as? BitmapDrawable)?.bitmap ?: return@target
                            binding.cropGifticon.setBitmap(bitmap)
                        }.build()
                    requireContext().imageLoader.enqueue(request)
                }
        }

        repeatOnStarted {
            combine(
                editorViewModel.selectedGifticon
                    .filterNotNull()
                    .map { editorViewModel.getGifticonData(it.id) }
                    .filterNotNull(),
                editorViewModel.selectedEditorChip.filterIsInstance<EditorChip.Property>()
            ) { data, editorChip ->
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
            }
        }
    }
}