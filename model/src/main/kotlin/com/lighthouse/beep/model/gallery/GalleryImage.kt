package com.lighthouse.beep.model.gallery

import android.net.Uri
import com.lighthouse.beep.model.gallery.adapter.getGalleryImageListAdapter
import com.squareup.moshi.JsonClass
import java.util.Date

@JsonClass(generateAdapter=true)
data class GalleryImage(
    val id: Long,
    val contentUri: Uri,
    val imagePath: String,
    val dateAdded: Date,
) {
    companion object {
        fun fromJson(json: String): List<GalleryImage> {
            val jsonAdapter = getGalleryImageListAdapter()
            return jsonAdapter.fromJson(json) ?: emptyList()
        }
    }
}
