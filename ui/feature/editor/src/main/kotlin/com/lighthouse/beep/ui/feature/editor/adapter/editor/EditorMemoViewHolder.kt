package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.ui.feature.editor.databinding.SectionEditorPreviewBinding
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip

internal class EditorMemoViewHolder(
    parent: ViewGroup,
    private val listener: OnEditorMemoListener,
    private val binding: SectionEditorPreviewBinding = SectionEditorPreviewBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : LifecycleViewHolder<EditorChip.Preview>(binding.root) {

    override fun onSetUpClickEvent(item: EditorChip.Preview) {
        binding.textMemo.setOnThrottleClickListener {
            listener.onMemoClick()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: EditorChip.Preview) {
        val param = EditType.MEMO.createTextInputParam(null)
        listener.getMemoFlow().collect(lifecycleOwner) { memo ->
            if (memo.isEmpty()) {
                binding.textMemo.setText(R.string.editor_gifticon_preview_memo_hint)
            } else {
                binding.textMemo.text = memo
            }
            binding.textMemoLength.text = "${memo.length}/${param.maxLength}"
        }
    }
}