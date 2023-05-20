package com.lighthouse.beep.data.remote.di

import com.lighthouse.beep.data.remote.repository.RemoteBrandDataSourceImpl
import com.lighthouse.beep.data.repository.brand.RemoteBrandDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsRemoteRepository(
        repository: RemoteBrandDataSourceImpl,
    ): RemoteBrandDataSource
}
