package com.lighthouse.beep.auth.kakao.di

import android.content.Context
import com.lighthouse.beep.auth.kakao.KakaoClient
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
    fun provideKakaoClient(
        @ApplicationContext context: Context,
    ): KakaoClient = KakaoClient(context)
}
