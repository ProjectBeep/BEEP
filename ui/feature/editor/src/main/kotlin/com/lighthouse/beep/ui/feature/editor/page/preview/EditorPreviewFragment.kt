package com.lighthouse.beep.ui.feature.editor.page.preview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import coil.load
import coil.size.Size
import com.lighthouse.beep.core.common.exts.cast
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.feature.editor.DialogProvider
import com.lighthouse.beep.ui.feature.editor.EditorViewModel
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.FragmentEditorPreviewBinding
import com.lighthouse.beep.ui.feature.editor.model.EditType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
internal class EditorPreviewFragment : Fragment(R.layout.fragment_editor_preview) {

    companion object {
        private val VIEW_RECT = RectF(0f, 0f, 80f.dp, 80f.dp)
    }

    private val viewModel by activityViewModels<EditorViewModel>()

    private val binding by viewBindings<FragmentEditorPreviewBinding>()

    private lateinit var dialogProvider: DialogProvider

    override fun onAttach(context: Context) {
        super.onAttach(context)

        dialogProvider = context.cast()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpPreview()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpPreview() {
        binding.imageThumbnail.clipToOutline = true
    }

    @SuppressLint("SetTextI18n")
    private fun setUpCollectState() {
        repeatOnStarted {
            viewModel.selectedGifticonDataFlow
                .map { it.originUri }
                .distinctUntilChanged()
                .collect { contentUri ->
                    binding.imageThumbnail.load(contentUri) {
                        size(Size.ORIGINAL)
                    }
                }
        }

        repeatOnStarted {
            viewModel.selectedGifticonDataFlow
                .map { it.cropData }
                .distinctUntilChanged()
                .collect { data ->
                    binding.imageThumbnail.imageMatrix = data.calculateMatrix(VIEW_RECT)
                }
        }

        repeatOnStarted {
            viewModel.selectedGifticonDataFlow
                .map { it.name }
                .distinctUntilChanged()
                .collect { name ->
                    binding.iconNameEmpty.isVisible = name.isEmpty()
                    binding.textName.text = name
                }
        }

        repeatOnStarted {
            viewModel.selectedGifticonDataFlow
                .map { it.brand }
                .distinctUntilChanged()
                .collect { brand ->
                    binding.iconBrandEmpty.isVisible = brand.isEmpty()
                    binding.textBrand.text = brand
                }
        }

        repeatOnStarted {
            viewModel.selectedGifticonDataFlow
                .map { it.displayExpired }
                .distinctUntilChanged()
                .collect { expired ->
                    binding.iconExpiredEmpty.isVisible = expired.isEmpty()
                    binding.textExpired.text = expired
                }
        }

        repeatOnStarted {
            viewModel.selectedGifticonDataFlow
                .map { it.memo }
                .distinctUntilChanged()
                .collect { memo ->
                    binding.textMemo.text = memo
                    binding.textMemoLength.text = "${memo.length}/${EditType.MEMO.maxLength}"
                }
        }
    }

    private fun setUpOnClickEvent() {
        binding.textMemo.setOnClickListener(createThrottleClickListener {
            dialogProvider.showTextInputDialog(EditType.MEMO)
        })
    }
}