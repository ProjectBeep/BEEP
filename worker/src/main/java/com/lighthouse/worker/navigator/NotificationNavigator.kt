package com.lighthouse.worker.navigator

import android.app.PendingIntent

//        val intent = Intent(applicationContext, Activity::class.java).apply {
//            putExtra(com.lighthouse.Const.KEY_GIFTICON_ID, gifticon.id)
//        }
//
//        val taskStack = TaskStackBuilder.create(applicationContext)
//            .addNextIntentWithParentStack(intent)
//            .getPendingIntent(gifticon.id.hashCode(), PendingIntent.FLAG_UPDATE_CURRENT)
interface NotificationNavigator {
    fun gotoDetail(gifticonId: String): PendingIntent
}
