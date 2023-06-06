package com.lighthouse.beep.auth.service.oauth

import com.lighthouse.beep.auth.model.AuthProvider
import com.lighthouse.beep.auth.network.NetworkTask
import javax.inject.Inject

internal class OAuthServiceProvider @Inject constructor(
    private val task: NetworkTask,
) {

    fun getOAuthService(provider: AuthProvider): OAuthService {
        return when (provider) {
            AuthProvider.NONE -> NoneOAuthService()
            AuthProvider.ANONYMOUS -> AnonymousOAuthService()
            AuthProvider.GOOGLE,
            AuthProvider.KAKAO,
            AuthProvider.NAVER,
            -> CustomTokenOAuthService(task)
        }
    }
}
