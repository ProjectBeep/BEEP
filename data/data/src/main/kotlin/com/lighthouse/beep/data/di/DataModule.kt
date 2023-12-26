package com.lighthouse.beep.data.di

import com.lighthouse.beep.data.repository.brand.BrandRepository
import com.lighthouse.beep.data.repository.brand.BrandRepositoryImpl
import com.lighthouse.beep.data.repository.device.DeviceRepository
import com.lighthouse.beep.data.repository.device.DeviceRepositoryImpl
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepositoryImpl
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.data.repository.gifticon.GifticonRepositoryImpl
import com.lighthouse.beep.data.repository.user.UserRepository
import com.lighthouse.beep.data.repository.user.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindsBrandRepository(
        repository: BrandRepositoryImpl,
    ): BrandRepository

    @Binds
    @Singleton
    abstract fun bindsGalleryImageRepository(
        repository: GalleryImageRepositoryImpl,
    ): GalleryImageRepository

    @Binds
    @Singleton
    abstract fun bindsGifticonRepository(
        repository: GifticonRepositoryImpl,
    ): GifticonRepository

    @Binds
    @Singleton
    abstract fun bindsDeviceRepository(
        repository: DeviceRepositoryImpl,
    ): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(
        repository: UserRepositoryImpl,
    ): UserRepository
}
