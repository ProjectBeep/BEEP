package com.lighthouse.auth.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GoogleOAuth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class KakaoOAuth

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class NaverOAuth

