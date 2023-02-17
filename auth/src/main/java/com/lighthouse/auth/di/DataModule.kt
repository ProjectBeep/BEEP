package com.lighthouse.auth.di

import com.lighthouse.auth.repository.AuthRepository
import com.lighthouse.auth.repository.AuthRepositoryImpl
import com.lighthouse.auth.repository.CipherTool
import com.lighthouse.auth.repository.CipherToolImpl
import com.lighthouse.auth.repository.GoogleClient
import com.lighthouse.auth.repository.GoogleClientImpl
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
        repository: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    abstract fun bindsCipherTool(
        tool: CipherToolImpl
    ): CipherTool

    @Binds
    abstract fun bindsGoogleClient(
        client: GoogleClientImpl
    ): GoogleClient
}
