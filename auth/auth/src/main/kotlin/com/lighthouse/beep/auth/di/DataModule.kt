package com.lighthouse.beep.auth.di

import com.lighthouse.beep.auth.repository.AuthRepository
import com.lighthouse.beep.auth.repository.AuthRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsAuthRepository(
        repository: AuthRepositoryImpl,
    ): AuthRepository
}
