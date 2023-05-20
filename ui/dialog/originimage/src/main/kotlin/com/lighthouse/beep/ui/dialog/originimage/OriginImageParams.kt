package com.lighthouse.beep.ui.dialog.originimage

import android.net.Uri
import android.os.Bundle
import com.lighthouse.beep.core.exts.getRequiredParcelableExtra

data class OriginImageParams(
    val uri: Uri,
) {

    fun buildBundle(): Bundle {
        return Bundle().apply {
            putParcelable(KEY_ORIGIN_IMAGE, uri)
        }
    }

    companion object {
        private const val KEY_ORIGIN_IMAGE = "Key.OriginImage"

        fun getUri(arguments: Bundle?): Uri? {
            return arguments?.getRequiredParcelableExtra(KEY_ORIGIN_IMAGE)
        }
    }
}
