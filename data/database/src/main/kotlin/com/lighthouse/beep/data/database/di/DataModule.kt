package com.lighthouse.beep.data.database.di

import com.lighthouse.beep.data.database.repository.brand.LocalBrandDataSourceImpl
import com.lighthouse.beep.data.database.repository.gifticon.LocalGifticonDataSourceImpl
import com.lighthouse.beep.data.repository.brand.LocalBrandDataSource
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class DataModule {

    @Binds
    abstract fun bindsBrandDataSource(
        repository: LocalBrandDataSourceImpl,
    ): LocalBrandDataSource

    @Binds
    abstract fun bindsGifticonDataSource(
        repository: LocalGifticonDataSourceImpl,
    ): LocalGifticonDataSource
}
