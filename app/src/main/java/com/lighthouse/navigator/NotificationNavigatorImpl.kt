package com.lighthouse.navigator

import android.app.Activity
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import com.lighthouse.worker.navigator.NotificationNavigator
import javax.inject.Inject

internal class NotificationNavigatorImpl @Inject constructor() : NotificationNavigator {

    // 왜 context 를 Hilt로 주입받지 않고 context 를 함수에 받아서 사용하는지 정리하기
    override fun gotoDetail(context: Context, gifticonId: String): PendingIntent {
        // Activity 를 실행할 Activity 로 변경하기
        val intent = Intent(context, Activity::class.java).apply {
            putExtra(com.lighthouse.Const.KEY_GIFTICON_ID, gifticonId)
        }

        return TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(intent)
            .getPendingIntent(gifticonId.hashCode(), PendingIntent.FLAG_UPDATE_CURRENT)
    }
}
