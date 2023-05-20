package com.lighthouse.auth.repository

import com.lighthouse.auth.di.GoogleOAuth
import com.lighthouse.beep.model.auth.AuthProvider
import javax.inject.Inject

internal class OAuthServiceProvider @Inject constructor(
    @GoogleOAuth private val googleOAuthService: OAuthService,
//    @NaverOAuth private val naverOAuthService: OAuthService,
//    @KakaoOAuth private val kakaoOAuthService: OAuthService,
) {

    fun getOAuthService(provider: AuthProvider): OAuthService {
        return googleOAuthService
//        return when (provider) {
//            AuthProvider.GOOGLE -> googleOAuthService
//            AuthProvider.NAVER -> naverOAuthService
//            AuthProvider.KAKAO -> kakaoOAuthService
//        }
    }
}
