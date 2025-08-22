package com.lighthouse.beep.data.local.di

import android.content.Context
import androidx.room.Room
import com.lighthouse.beep.data.local.database.BeepDatabase
import com.lighthouse.beep.data.local.database.dao.BrandLocationDao
import com.lighthouse.beep.data.local.database.dao.GalleryImageDataDao
import com.lighthouse.beep.data.local.database.dao.GifticonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
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
    @Singleton
    fun provideGifticonDao(
        database: BeepDatabase,
    ): GifticonDao = database.gifticonDao()

    @Provides
    @Singleton
    fun provideBrandLocationDao(
        database: BeepDatabase,
    ): BrandLocationDao = database.brandLocationDao()

    @Provides
    @Singleton
    fun provideGalleryRecognizeDao(
        database: BeepDatabase
    ): GalleryImageDataDao = database.galleryImageDataDao()
}
