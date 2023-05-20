package com.lighthouse.data.encryptedpreference.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Suppress("UNUSED")
@Module
@InstallIn(SingletonComponent::class)
internal object PreferenceModule {

    private const val ENCRYPTED_SETTINGS = "encrypted_settings"

    @Provides
    fun providePreferencesDataStore(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        val masterKey = MasterKey(context = context)
        return EncryptedSharedPreferences(context, ENCRYPTED_SETTINGS, masterKey)
    }
}
