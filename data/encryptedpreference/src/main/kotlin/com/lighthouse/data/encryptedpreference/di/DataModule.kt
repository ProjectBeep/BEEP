package com.lighthouse.data.encryptedpreference.di

import com.lighthouse.beep.data.repository.user.LocalUserAuthDataSource
import com.lighthouse.data.encryptedpreference.repository.LocalUserAuthDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsLocalUserAuthRepository(
        repository: LocalUserAuthDataSourceImpl,
    ): LocalUserAuthDataSource
}
