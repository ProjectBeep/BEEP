package com.lighthouse.beep.data.local.di

import com.lighthouse.beep.data.local.repository.brand.LocalBrandDataSourceImpl
import com.lighthouse.beep.data.local.repository.device.LocalDeviceDataSourceImpl
import com.lighthouse.beep.data.local.repository.gallery.GalleryImagePagingSourceImpl
import com.lighthouse.beep.data.local.repository.gifticon.LocalGifticonDataSourceImpl
import com.lighthouse.beep.data.local.repository.gifticon.LocalGifticonStorageImpl
import com.lighthouse.beep.data.local.repository.user.LocalUserDataSourceImpl
import com.lighthouse.beep.data.repository.brand.LocalBrandDataSource
import com.lighthouse.beep.data.repository.device.LocalDeviceDataSource
import com.lighthouse.beep.data.repository.gallery.GalleryImagePagingSource
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonDataSource
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonStorage
import com.lighthouse.beep.data.repository.user.LocalUserDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsDeviceDataSource(
        dataSource: LocalDeviceDataSourceImpl,
    ): LocalDeviceDataSource

    @Binds
    abstract fun bindsUserDataSource(
        dataSource: LocalUserDataSourceImpl,
    ): LocalUserDataSource

    @Binds
    abstract fun bindsGalleryImagePagingSource(
        pagingSource: GalleryImagePagingSourceImpl,
    ): GalleryImagePagingSource

    @Binds
    abstract fun bindsLocalGifticonStorage(
        storage: LocalGifticonStorageImpl,
    ): LocalGifticonStorage

    @Binds
    abstract fun bindsBrandDataSource(
        repository: LocalBrandDataSourceImpl,
    ): LocalBrandDataSource

    @Binds
    abstract fun bindsGifticonDataSource(
        repository: LocalGifticonDataSourceImpl,
    ): LocalGifticonDataSource
}
