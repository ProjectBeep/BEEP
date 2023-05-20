package com.lighthouse.beep.data.database.di

import android.content.Context
import androidx.room.Room
import com.lighthouse.beep.data.database.BeepDatabase
import com.lighthouse.beep.data.database.dao.BrandLocationDao
import com.lighthouse.beep.data.database.dao.GifticonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    fun provideBeepDatabase(
        @ApplicationContext context: Context,
    ): BeepDatabase {
        return Room.databaseBuilder(
            context,
            BeepDatabase::class.java,
            BeepDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    fun provideGifticonDao(
        database: BeepDatabase,
    ): GifticonDao = database.gifticonDao()

    @Provides
    fun provideBrandLocationDao(
        database: BeepDatabase,
    ): BrandLocationDao = database.brandLocationDao()
}
