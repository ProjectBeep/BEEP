package com.lighthouse.beep.auth.di

import com.lighthouse.beep.auth.data.AuthRepositoryImpl
import com.lighthouse.beep.auth.data.GifticonRepository
import com.lighthouse.beep.auth.data.RemoteAuthDataSource
import com.lighthouse.beep.auth.network.NetworkTask
import com.lighthouse.beep.domain.repository.auth.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AuthModule {

    @Binds
    @Singleton
    abstract fun bindsAuthRepository(
        repository: AuthRepositoryImpl,
    ): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
internal object AuthNetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideNetworkTask(client: OkHttpClient): NetworkTask {
        return NetworkTask(client)
    }

    @Provides
    @Singleton
    fun provideRemoteAuthDataSource(
        httpClient: OkHttpClient,
        networkTask: NetworkTask,
    ): RemoteAuthDataSource {
        return RemoteAuthDataSource(httpClient, networkTask)
    }

    @Provides
    @Singleton
    fun provideGifticonRepository(): GifticonRepository {
        return object : GifticonRepository {
            override suspend fun deleteGifticon(userUid: String) {
                // TODO: 실제 GifticonRepository를 주입받도록 개선 필요
                // 현재는 순환 의존성을 피하기 위해 빈 구현체 사용
            }
        }
    }
}