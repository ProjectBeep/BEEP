package com.lighthouse.beep.model.gallery

import android.net.Uri
import java.util.Date

data class GalleryImage(
    val id: Long,
    val contentUri: Uri,
    val date: Date,
)
