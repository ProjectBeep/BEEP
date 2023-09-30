package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.R
import com.lighthouse.beep.theme.R as ThemeR
import com.lighthouse.beep.ui.feature.editor.databinding.SectionEditorPreviewBinding
import com.lighthouse.beep.ui.feature.editor.model.EditorChip

internal class EditorPreviewViewHolder(
    parent: ViewGroup,
    private val listener: OnEditorPreviewListener,
    private val binding: SectionEditorPreviewBinding = SectionEditorPreviewBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
) : LifecycleViewHolder<EditorChip.Preview>(binding.root) {

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: EditorChip.Preview) {
        listener.getInvalidPropertyFlow().collect(lifecycleOwner) { list ->
            val items = if (list.isEmpty()) {
                context.getString(R.string.editor_preview_property_all)
            } else {
                list.joinToString { context.getString(it.textResId) }
            }

            val text = if (list.isEmpty()) {
                context.getString(R.string.editor_preview_complete, items)
            } else {
                context.getString(R.string.editor_preview_invalid_items, items)
            }

            binding.textInvalidProperty.text = SpannableString(text).apply {
                val color = ForegroundColorSpan(context.getColor(ThemeR.color.beep_pink))
                setSpan(color, 0, items.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
    }
}