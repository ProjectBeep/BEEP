package com.lighthouse.features.common.navigator

import android.content.Context
import androidx.activity.result.ActivityResult

interface AddGifticonNavigator {

    fun openAddGifticon(context: Context, result: (ActivityResult) -> Unit)
}
