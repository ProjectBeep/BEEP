package com.lighthouse.beep.ui.feature.editor.adapter.editor

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.EditorChipDiff
import com.lighthouse.beep.ui.feature.editor.model.EditType

internal class EditorAdapter(
    private val requestManager: RequestManager,
    private val onEditorMemoListener: OnEditorMemoListener,
    private val onEditorThumbnailListener: OnEditorThumbnailListener,
    private val onEditorTextListener: OnEditorTextListener,
) : ListAdapter<EditorChip, RecyclerView.ViewHolder>(EditorChipDiff()){

    companion object {
        private const val TYPE_PREVIEW = 1
        private const val TYPE_THUMBNAIL = 2
        private const val TYPE_TEXT = 3
    }

    override fun getItemViewType(position: Int): Int {
        return when(val item = getItem(position)) {
            is EditorChip.Preview -> TYPE_PREVIEW
            is EditorChip.Property -> if (item.type == EditType.THUMBNAIL) {
                TYPE_THUMBNAIL
            } else {
                TYPE_TEXT
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PREVIEW -> EditorMemoViewHolder(parent, onEditorMemoListener)
            TYPE_THUMBNAIL -> EditorThumbnailViewHolder(parent, requestManager, onEditorThumbnailListener)
            TYPE_TEXT -> EditorTextViewHolder(parent, onEditorTextListener)
            else -> throw RuntimeException("${javaClass.simpleName}에 정의 되지 않는 item 입니다")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is EditorMemoViewHolder && item is EditorChip.Preview -> holder.bind(item)
            holder is EditorThumbnailViewHolder && item is EditorChip.Property -> holder.bind(item)
            holder is EditorTextViewHolder && item is EditorChip.Property -> holder.bind(item)
        }
    }
}