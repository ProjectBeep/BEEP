package com.lighthouse.features.home.model

import android.net.Uri
import com.lighthouse.core.android.utils.resource.UIText

internal data class UIGifticonWithDistance(
    val id: String,
    val croppedUri: Uri?,
    val body: UIText,
    val brand: String,
    val expireAt: String,
    val dDay: UIText,
    val distance: UIText
)
