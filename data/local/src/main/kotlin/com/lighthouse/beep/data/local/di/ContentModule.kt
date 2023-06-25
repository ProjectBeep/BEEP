package com.lighthouse.beep.data.local.di

import android.content.ContentResolver
import android.content.Context
import com.lighthouse.beep.data.local.repository.gallery.GalleryImageDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal object ContentModule {

    @Provides
    fun provideContentResolver(
        @ApplicationContext context: Context,
    ): ContentResolver {
        return context.contentResolver
    }

    @Provides
    fun providesGalleryImageDataSource(
        contentResolver: ContentResolver,
    ): GalleryImageDataSource = GalleryImageDataSource(contentResolver)
}
