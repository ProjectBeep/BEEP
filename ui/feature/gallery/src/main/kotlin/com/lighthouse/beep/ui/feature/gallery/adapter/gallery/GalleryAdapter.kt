package com.lighthouse.beep.ui.feature.gallery.adapter.gallery

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lighthouse.beep.model.gallery.GalleryImage
import kotlin.math.min

internal class GalleryAdapter : RecyclerView.Adapter<GalleryViewHolder>() {

    private val list = mutableListOf<GalleryImage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(parent)
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun refreshList(newList: List<GalleryImage>) {
        val oldListSize = list.size
        list.clear()
        list.addAll(newList)
        if (oldListSize > newList.size) {
            notifyItemRangeRemoved(newList.size, oldListSize - newList.size)
        } else if(oldListSize < newList.size) {
            notifyItemRangeInserted(oldListSize, newList.size - oldListSize)
        }
        notifyItemRangeChanged(0, min(oldListSize, newList.size))
    }

    fun appendList(newList: List<GalleryImage>) {
        val oldListSize = list.size
        list.addAll(newList)
        notifyItemRangeInserted(oldListSize, newList.size)
    }
}