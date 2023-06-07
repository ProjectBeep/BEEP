package com.lighthouse.beep.auth.service.signout

import javax.inject.Inject

internal class KakaoSignOutService @Inject constructor() : SignOutService {

    override suspend fun signOut() = Unit
}