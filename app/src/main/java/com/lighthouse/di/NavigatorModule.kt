package com.lighthouse.di

import com.lighthouse.navigator.NotificationNavigatorImpl
import com.lighthouse.worker.navigator.NotificationNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class NavigatorModule {

    @Binds
    abstract fun bindsNotificationNavigator(
        navigator: NotificationNavigatorImpl
    ): NotificationNavigator
}
