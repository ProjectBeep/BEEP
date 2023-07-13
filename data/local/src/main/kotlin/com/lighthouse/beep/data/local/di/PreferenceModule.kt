package com.lighthouse.beep.data.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.lighthouse.beep.data.local.serializer.DeviceConfigSerializer
import com.lighthouse.beep.model.deviceconfig.DeviceConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Suppress("UNUSED")
@Module
@InstallIn(SingletonComponent::class)
internal object PreferenceModule {

    private const val DEVICE_CONFIG_FILE_NAME = "deviceConfig.json"

    private val Context.deviceConfigDataStore by dataStore(
        DEVICE_CONFIG_FILE_NAME,
        DeviceConfigSerializer,
    )

    @Provides
    fun provideDeviceConfigDataStore(@ApplicationContext context: Context): DataStore<DeviceConfig> {
        return context.deviceConfigDataStore
    }
}
