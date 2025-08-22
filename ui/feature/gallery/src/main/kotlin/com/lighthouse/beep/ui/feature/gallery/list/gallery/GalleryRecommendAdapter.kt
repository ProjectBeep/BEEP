package com.lighthouse.beep.ui.feature.gallery.list.gallery

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.lighthouse.beep.ui.feature.gallery.model.GalleryItem
import com.lighthouse.beep.ui.feature.gallery.model.GalleryItemDiff

internal class GalleryRecommendAdapter(
    private val requestManager: RequestManager,
    private val onGalleryAddItemListener: OnGalleryAddItemListener,
    private val onGalleryItemListener: OnGalleryItemListener,
) : ListAdapter<GalleryItem, RecyclerView.ViewHolder>(GalleryItemDiff()) {

    companion object {
        private const val TYPE_ADD_ITEM = 1
        private const val TYPE_ITEM = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)) {
            is GalleryItem.AddItem -> TYPE_ADD_ITEM
            is GalleryItem.Image -> TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            TYPE_ADD_ITEM -> GalleryAddViewHolder(parent, onGalleryAddItemListener)
            TYPE_ITEM -> GalleryViewHolder(parent, requestManager, onGalleryItemListener)
            else -> throw RuntimeException("${javaClass.simpleName}에 정의 되지 않는 item 입니다")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when {
            holder is GalleryAddViewHolder && item is GalleryItem.AddItem -> holder.bind(item)
            holder is GalleryViewHolder && item is GalleryItem.Image -> holder.bind(item)
        }
    }

    fun getItemByPosition(position: Int): GalleryItem.Image? {
        if (position == -1) {
            return null
        }
        return getItem(position) as? GalleryItem.Image
    }
}