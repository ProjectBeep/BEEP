package com.lighthouse.worker.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.lighthouse.beep.model.gifticon.GifticonNotification
import com.lighthouse.core.utils.time.TimeCalculator
import com.lighthouse.domain.usecase.GetGifticonsWithDDayUseCase
import com.lighthouse.features.common.navigator.DetailPendingNavigator
import com.lighthouse.worker.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
import java.util.concurrent.TimeUnit

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val getGifticonsUseCase: GetGifticonsWithDDayUseCase,
    private val navigator: DetailPendingNavigator
) : CoroutineWorker(context, workerParams) {

    private val notificationManager by lazy {
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
    }

    override suspend fun doWork(): Result {
        createNotificationChannel()

        val gifticons = getGifticonsUseCase.invoke().first()
        for (gifticon in gifticons) {
            createNotification(gifticon)
        }
        return Result.success()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                applicationContext.getString(R.string.expired_channel_id),
                applicationContext.getString(R.string.expired_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = applicationContext.getString(R.string.expired_channel_description)
            }
            notificationManager?.createNotificationChannel(channel)
        }
    }

    private fun createNotification(gifticon: GifticonNotification) {
        val notification = NotificationCompat.Builder(
            applicationContext,
            applicationContext.getString(R.string.expired_channel_id)
        ).setSmallIcon(com.lighthouse.beep.common.android.R.drawable.ic_splash_beep)
            .setContentTitle(applicationContext.getString(R.string.expired_title))
            .setContentText(
                applicationContext.getString(
                    R.string.expired_description,
                    gifticon.name,
                    gifticon.dDay
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(navigator.getPendingIntent(applicationContext, gifticon.id))
            .build().apply {
                flags = Notification.FLAG_AUTO_CANCEL
            }

        notificationManager?.notify(gifticon.id.hashCode(), notification)
    }

    companion object {
        private const val DAY_INTERVAL = 24L

        val request = PeriodicWorkRequestBuilder<NotificationWorker>(
            DAY_INTERVAL,
            TimeUnit.HOURS
        ).setInitialDelay(
            TimeCalculator.calculateAfterDateDiffHour(date = 1, hour = 9),
            TimeUnit.HOURS
        ).build()
    }
}
