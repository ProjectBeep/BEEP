package com.lighthouse.worker.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lighthouse.domain.usecase.gifticon.remove.RemoveExpirationBrandUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ExpirationBrandWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val removeExpirationBrandUseCase: RemoveExpirationBrandUseCase
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val result = removeExpirationBrandUseCase()
        return if (result.isSuccess) {
            Result.success()
        } else {
            Result.retry()
        }
    }
}
