package com.lighthouse.beep.auth.di

import com.google.firebase.auth.FirebaseAuth
import com.lighthouse.beep.auth.network.NetworkTask
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    fun provideNetworkTask(
        client: OkHttpClient,
    ): NetworkTask {
        return NetworkTask(client)
    }
}
