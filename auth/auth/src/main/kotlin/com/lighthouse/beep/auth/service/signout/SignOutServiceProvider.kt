package com.lighthouse.beep.auth.service.signout

import com.lighthouse.beep.auth.model.AuthProvider
import javax.inject.Inject

internal class SignOutServiceProvider @Inject constructor() {

    fun getSignOutService(provider: AuthProvider): SignOutService {
        return when (provider) {
            AuthProvider.NONE -> NoneSignOutService()
            AuthProvider.ANONYMOUS -> AnonymousSignOutService()
            AuthProvider.GOOGLE,
            AuthProvider.KAKAO,
            AuthProvider.NAVER,
            -> AnonymousSignOutService()
        }
    }
}
