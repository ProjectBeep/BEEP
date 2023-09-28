package com.lighthouse.beep.ui.feature.editor.model

import androidx.annotation.StringRes
import com.lighthouse.beep.ui.feature.editor.R

internal enum class PropertyType(@StringRes val textResId: Int) {
    THUMBNAIL(R.string.editor_gifticon_property_thumbnail),
    NAME(R.string.editor_gifticon_property_thumbnail),
    BRAND(R.string.editor_gifticon_property_thumbnail),
    BARCODE(R.string.editor_gifticon_property_thumbnail),
    EXPIRED(R.string.editor_gifticon_property_thumbnail),
}