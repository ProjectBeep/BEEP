package com.lighthouse.auth.google.di

import com.lighthouse.auth.di.GoogleOAuth
import com.lighthouse.auth.google.repository.GoogleClient
import com.lighthouse.auth.google.repository.GoogleOAuthService
import com.lighthouse.auth.repository.OAuthService
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
        client: GoogleOAuthService,
    ): GoogleClient

    @GoogleOAuth
    @Binds
    abstract fun bindsGoogleOAuthRepository(
        repository: GoogleOAuthService,
    ): OAuthService
}