package com.lighthouse.beep.content.di

import com.lighthouse.beep.content.repository.gallery.GalleryImagePagingSourceImpl
import com.lighthouse.beep.content.repository.gifticon.LocalGifticonStorageImpl
import com.lighthouse.beep.data.repository.gallery.GalleryImagePagingSource
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsGalleryImagePagingSource(
        pagingSource: GalleryImagePagingSourceImpl,
    ): GalleryImagePagingSource

    @Binds
    abstract fun bindsLocalGifticonStorage(
        storage: LocalGifticonStorageImpl,
    ): LocalGifticonStorage
}
