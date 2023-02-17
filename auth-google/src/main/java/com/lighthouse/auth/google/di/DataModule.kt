package com.lighthouse.auth.google.di

import com.lighthouse.auth.google.repository.GoogleClient
import com.lighthouse.auth.google.repository.GoogleClientImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsGoogleClient(
        client: GoogleClientImpl
    ): GoogleClient
}
