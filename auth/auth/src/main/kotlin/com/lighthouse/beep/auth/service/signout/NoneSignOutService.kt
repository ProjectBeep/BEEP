package com.lighthouse.beep.auth.service.signout

internal class NoneSignOutService : SignOutService {

    override suspend fun signOut() {
        throw IllegalStateException("잘 못된 Provider 입니다.")
    }
}
