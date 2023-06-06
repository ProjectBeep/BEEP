package com.lighthouse.beep.auth.service.signout

import javax.inject.Inject

internal class NaverSignOutService @Inject constructor() : SignOutService {

    override suspend fun signOut() = Unit
}
