package com.lighthouse.worker.di

import com.lighthouse.domain.controller.NotificationWorkerController
import com.lighthouse.worker.controller.NotificationWorkerControllerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class ControllerModule {

    @Binds
    abstract fun bindsNotificationWorkerController(
        controller: NotificationWorkerControllerImpl
    ): NotificationWorkerController
}
