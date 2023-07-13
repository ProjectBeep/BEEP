package com.lighthouse.beep.auth.service.oauth

import com.lighthouse.beep.auth.network.NetworkTask
import com.lighthouse.beep.model.deviceconfig.AuthProvider
import javax.inject.Inject

internal class OAuthServiceProvider @Inject constructor(
    private val task: NetworkTask,
) {

    fun getOAuthService(provider: AuthProvider): OAuthService {
        return when (provider) {
            AuthProvider.NONE -> NoneOAuthService()
            AuthProvider.GUEST -> AnonymousOAuthService()
            AuthProvider.GOOGLE,
            AuthProvider.KAKAO,
            AuthProvider.NAVER,
            -> CustomTokenOAuthService(task)
        }
    }
}
