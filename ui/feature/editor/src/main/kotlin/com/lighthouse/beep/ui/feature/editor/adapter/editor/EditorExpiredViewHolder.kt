package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.recyclerview.viewholder.LifecycleViewHolder
import com.lighthouse.beep.ui.feature.editor.databinding.SectionEditorExpiredBinding
import com.lighthouse.beep.ui.feature.editor.model.EditorChip

internal class EditorExpiredViewHolder(
    parent: ViewGroup,
    private val listener: OnEditorExpiredListener,
    private val binding: SectionEditorExpiredBinding = SectionEditorExpiredBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
    )
): LifecycleViewHolder<EditorChip.Property>(binding.root) {

    override fun onSetUpClickEvent(item: EditorChip.Property) {
        binding.containerExpired.setOnThrottleClickListener {
            listener.showExpired()
        }
    }

    override fun onCollectState(lifecycleOwner: LifecycleOwner, item: EditorChip.Property) {
        listener.getGifticonDataFlow().collect(lifecycleOwner) { data ->
            binding.textYear.text = data.expiredYear
            binding.textMonth.text = data.expiredMonth
            binding.textDate.text = data.expiredDate
        }
    }
}