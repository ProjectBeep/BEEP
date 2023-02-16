package com.lighthouse.di

import com.lighthouse.features.common.navigator.DetailPendingNavigator
import com.lighthouse.features.common.navigator.MainNavigator
import com.lighthouse.navigator.DetailPendingNavigatorImpl
import com.lighthouse.navigator.MainNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class NavigatorModule {

    @Binds
    abstract fun bindsDetailPendingNavigator(
        navigator: DetailPendingNavigatorImpl
    ): DetailPendingNavigator

    @Binds
    abstract fun bindsMainNavigator(
        navigator: MainNavigatorImpl
    ): MainNavigator
}
