package com.lighthouse.worker.controller

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.lighthouse.domain.controller.NotificationWorkerController
import com.lighthouse.worker.workers.NotificationWorker
import javax.inject.Inject

internal class NotificationWorkerControllerImpl @Inject constructor(
    private val workManager: WorkManager
) : NotificationWorkerController {

    override fun enqueue() {
        workManager.enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            NotificationWorker.request
        )
    }

    override fun cancel() {
        workManager.cancelUniqueWork(WORKER_NAME)
    }

    companion object {
        private const val WORKER_NAME = "notification"
    }
}
