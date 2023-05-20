package com.lighthouse.beep.data.di

import com.lighthouse.beep.data.repository.brand.BrandRepositoryImpl
import com.lighthouse.beep.data.repository.gallery.GalleryImageRepositoryImpl
import com.lighthouse.beep.data.repository.gifticon.GifticonRecognizeRepositoryImpl
import com.lighthouse.beep.data.repository.gifticon.GifticonRepositoryImpl
import com.lighthouse.beep.data.repository.user.UserRepositoryImpl
import com.lighthouse.beep.domain.repository.brand.BrandRepository
import com.lighthouse.beep.domain.repository.gallery.GalleryImageRepository
import com.lighthouse.beep.domain.repository.gifticon.GifticonRecognizeRepository
import com.lighthouse.beep.domain.repository.gifticon.GifticonRepository
import com.lighthouse.beep.domain.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsBrandRepository(
        repository: BrandRepositoryImpl,
    ): BrandRepository

    @Binds
    abstract fun bindsGalleryImageRepository(
        repository: GalleryImageRepositoryImpl,
    ): GalleryImageRepository

    @Binds
    abstract fun bindsGifticonRecognizeRepository(
        repository: GifticonRecognizeRepositoryImpl,
    ): GifticonRecognizeRepository

    @Binds
    abstract fun binsGifticonRepository(
        repository: GifticonRepositoryImpl,
    ): GifticonRepository

    @Binds
    abstract fun bindsUserRepository(
        repository: UserRepositoryImpl,
    ): UserRepository
}
