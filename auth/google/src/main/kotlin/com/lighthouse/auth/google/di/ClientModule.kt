package com.lighthouse.auth.google.di

import android.content.Context
import com.lighthouse.auth.google.GoogleClient
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
    fun provideGoogleClient(
        @ApplicationContext context: Context,
    ): GoogleClient = GoogleClient(context)
}
