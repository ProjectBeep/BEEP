package com.lighthouse.beep.auth.naver.di

import android.content.Context
import com.lighthouse.beep.auth.naver.NaverClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
object ClientModule {

    @Provides
    fun provideNaverClient(
        @ApplicationContext context: Context,
    ): NaverClient = NaverClient(context)
}
