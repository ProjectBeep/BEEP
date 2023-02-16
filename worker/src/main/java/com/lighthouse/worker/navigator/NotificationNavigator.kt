package com.lighthouse.worker.navigator

import android.app.PendingIntent
import android.content.Context

interface NotificationNavigator {
    fun gotoDetail(context: Context, gifticonId: String): PendingIntent
}
