package com.lighthouse.di

import com.lighthouse.features.common.navigator.AddGifticonNavigator
import com.lighthouse.features.common.navigator.DetailNavigator
import com.lighthouse.features.common.navigator.DetailPendingNavigator
import com.lighthouse.features.common.navigator.GifticonListNavigator
import com.lighthouse.features.common.navigator.HomeNavigator
import com.lighthouse.features.common.navigator.MainNavigator
import com.lighthouse.features.common.navigator.MapNavigator
import com.lighthouse.features.common.navigator.SecurityNavigator
import com.lighthouse.features.common.navigator.SettingNavigator
import com.lighthouse.navigator.AddGifticonNavigatorImpl
import com.lighthouse.navigator.DetailNavigatorImpl
import com.lighthouse.navigator.DetailPendingNavigatorImpl
import com.lighthouse.navigator.GifticonListNavigatorImpl
import com.lighthouse.navigator.HomeNavigatorImpl
import com.lighthouse.navigator.MainNavigatorImpl
import com.lighthouse.navigator.MapNavigatorImpl
import com.lighthouse.navigator.SecurityNavigatorImpl
import com.lighthouse.navigator.SettingNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@Module
@InstallIn(SingletonComponent::class)
internal abstract class NavigatorModule {

    @Binds
    abstract fun bindsAddGifticonNavigator(
        navigator: AddGifticonNavigatorImpl
    ): AddGifticonNavigator

    @Binds
    abstract fun bindsDetailNavigator(
        navigator: DetailNavigatorImpl
    ): DetailNavigator

    @Binds
    abstract fun bindsDetailPendingNavigator(
        navigator: DetailPendingNavigatorImpl
    ): DetailPendingNavigator

    @Binds
    abstract fun bindsGifticonListNavigator(
        navigator: GifticonListNavigatorImpl
    ): GifticonListNavigator

    @Binds
    abstract fun bindsHomeNavigator(
        navigator: HomeNavigatorImpl
    ): HomeNavigator

    @Binds
    abstract fun bindsMainNavigator(
        navigator: MainNavigatorImpl
    ): MainNavigator

    @Binds
    abstract fun bindsMapNavigator(
        navigator: MapNavigatorImpl
    ): MapNavigator

    @Binds
    abstract fun bindsSecurityNavigator(
        navigator: SecurityNavigatorImpl
    ): SecurityNavigator

    @Binds
    abstract fun bindsSettingNavigator(
        navigator: SettingNavigatorImpl
    ): SettingNavigator
}
