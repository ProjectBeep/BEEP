package com.lighthouse.features.common.navigator

import android.app.PendingIntent
import android.content.Context

interface DetailPendingNavigator {
    fun getPendingIntent(context: Context, gifticonId: String): PendingIntent
}
