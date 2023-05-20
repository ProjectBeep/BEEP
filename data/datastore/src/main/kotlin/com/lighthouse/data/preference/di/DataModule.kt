package com.lighthouse.data.preference.di

import com.lighthouse.beep.data.repository.user.LocalUserDataSource
import com.lighthouse.data.preference.repository.LocalUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsUserPreferenceRepository(
        repository: LocalUserDataSourceImpl,
    ): LocalUserDataSource
}
