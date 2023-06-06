package com.lighthouse.beep.auth.service.signout

internal class AnonymousSignOutService : SignOutService {

    override suspend fun signOut() = Unit
}
