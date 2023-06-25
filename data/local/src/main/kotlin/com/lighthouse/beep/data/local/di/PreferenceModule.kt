package com.lighthouse.beep.data.local.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
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
    private const val USER_PREFERENCES = "user_preferences"

    @Provides
    fun provideEncryptedSharedPreferencesDataStore(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        val masterKey = MasterKey(context = context)
        return EncryptedSharedPreferences(context, ENCRYPTED_SETTINGS, masterKey)
    }

    @Provides
    fun providePreferencesDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(USER_PREFERENCES) },
        )
    }
}
