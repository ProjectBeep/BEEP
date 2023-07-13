package com.lighthouse.beep.auth.service.signout

import com.lighthouse.beep.model.deviceconfig.AuthProvider
import javax.inject.Inject

internal class SignOutServiceProvider @Inject constructor() {

    fun getSignOutService(provider: AuthProvider): SignOutService {
        return when (provider) {
            AuthProvider.NONE -> NoneSignOutService()
            AuthProvider.GUEST -> AnonymousSignOutService()
            AuthProvider.GOOGLE,
            AuthProvider.KAKAO,
            AuthProvider.NAVER,
            -> AnonymousSignOutService()
        }
    }
}
