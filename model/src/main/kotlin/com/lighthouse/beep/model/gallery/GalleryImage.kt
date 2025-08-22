package com.lighthouse.beep.model.gallery

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil
import com.lighthouse.beep.model.gallery.json.getGalleryImageListAdapter
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter=true)
data class GalleryImage(
    val id: Long,
    val contentUri: Uri,
    val imagePath: String,
    val dateAdded: Date,
) {
    val gifticonImageDataKey = "${imagePath}-${dateAdded.time}"

    companion object {
        fun fromJson(json: String): List<GalleryImage> {
            if (json.isEmpty()) {
                return emptyList()
            }
            val jsonAdapter = getGalleryImageListAdapter()
            return jsonAdapter.fromJson(json) ?: emptyList()
        }
    }
}

class GalleryImageDiff : DiffUtil.ItemCallback<GalleryImage>() {

    override fun areItemsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: GalleryImage, newItem: GalleryImage): Boolean {
        return oldItem == newItem
    }
}
