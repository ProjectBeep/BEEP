package com.lighthouse.beep.di

import com.lighthouse.beep.navs.AppNavigator
import com.lighthouse.beep.navs.AppNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class NavModule {

    @Binds
    abstract fun bindAppNavigator(
        navigator: AppNavigatorImpl,
    ): AppNavigator
}