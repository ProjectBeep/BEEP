package com.lighthouse.beep.core.ui.content

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

internal class GalleryContentObserver(
    lifecycleOwner: LifecycleOwner,
    contentResolver: ContentResolver,
    private val listener: OnContentChangeListener,
) {
    private val handler = Handler(Looper.getMainLooper())

    private val contentObserver = object: ContentObserver(handler) {
        override fun onChange(selfChange: Boolean, uri: Uri?, flags: Int) {
            val id = getContentId(uri) ?: return
            when {
                flags and ContentResolver.NOTIFY_INSERT == ContentResolver.NOTIFY_INSERT -> {
                    /**
                     * NOTIFY_INSERT 이후 NOTIFY_UPDATE 가 2번 들어 오는데
                     * 값을 전부 받아야 이미지를 갱신 할 수 있음
                     */
                    Log.d("ContentObserver", "insert : $id")
                    handler.postDelayed({
                        listener.onInsert(id)
                    }, 100)
                }
                flags and ContentResolver.NOTIFY_DELETE == ContentResolver.NOTIFY_DELETE -> {
                    Log.d("ContentObserver", "delete : $id")
                    listener.onDelete(id)
                }
            }
        }

        private fun getContentId(uri: Uri?): Long? {
            return uri?.lastPathSegment?.toLong()
        }
    }

    private val lifecycleObserver = object: DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            contentResolver.unregisterContentObserver(contentObserver)
            owner.lifecycle.removeObserver(this)
        }
    }

    init {
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            true,
            contentObserver,
        )
    }
}

fun ComponentActivity.registerGalleryContentObserver(
    listener: OnContentChangeListener,
) {
    GalleryContentObserver(
        lifecycleOwner = this,
        contentResolver = contentResolver,
        listener = listener,
    )
}

fun Fragment.registerGalleryContentObserver(
    listener: OnContentChangeListener,
) {
    GalleryContentObserver(
        lifecycleOwner = this,
        contentResolver = requireContext().contentResolver,
        listener = listener,
    )
}

interface OnContentChangeListener {
    fun onInsert(id: Long)

    fun onDelete(id: Long)
}