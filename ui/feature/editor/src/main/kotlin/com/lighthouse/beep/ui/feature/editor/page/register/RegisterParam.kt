package com.lighthouse.beep.ui.feature.editor.page.register

import android.content.Context
import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.json.toJson
import com.lighthouse.beep.navs.AppNavParam

class RegisterParam(
    private val list: List<GalleryImage> = emptyList()
): AppNavParam {

    companion object {
        private const val KEY_GALLERY_IMAGE_LIST = "Key.GalleryImageList"

        fun getGalleryList(savedStateHandle: SavedStateHandle): List<GalleryImage> {
            val json = savedStateHandle.get<String>(KEY_GALLERY_IMAGE_LIST) ?: ""
            return GalleryImage.fromJson(json)
        }
    }

    override fun createIntent(context: Context): Intent {
        return Intent(context, RegisterActivity::class.java).apply {
            putExtra(KEY_GALLERY_IMAGE_LIST, list.toJson())
        }
    }
}