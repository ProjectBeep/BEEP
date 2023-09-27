package com.lighthouse.beep.navs.result

import android.content.Intent
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.adapter.toJson

class EditorResult(
    val list: List<GalleryImage>
) {

    companion object {
        private const val KEY_GALLERY_IMAGE_LIST = "Key.GalleryImageList"

        private fun getGalleryList(intent: Intent?): List<GalleryImage> {
            val json = intent?.getStringExtra(KEY_GALLERY_IMAGE_LIST) ?: ""
            return GalleryImage.fromJson(json)
        }
    }

    constructor(intent: Intent?) : this (getGalleryList(intent))

    fun createIntent(): Intent {
        return Intent().apply {
            putExtra(KEY_GALLERY_IMAGE_LIST, list.toJson())
        }
    }
}