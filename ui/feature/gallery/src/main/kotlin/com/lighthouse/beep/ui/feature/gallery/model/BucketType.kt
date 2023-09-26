package com.lighthouse.beep.ui.feature.gallery.model

import androidx.annotation.StringRes
import com.lighthouse.beep.ui.feature.gallery.R

internal enum class BucketType(
    @StringRes val titleRes: Int,
)  {

    RECOMMEND(R.string.bucket_type_recommend),
    ALL(R.string.bucket_type_all),
}