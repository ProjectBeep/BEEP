package com.lighthouse.beep.auth.service.oauth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.lighthouse.beep.auth.model.OAuthRequest

internal class NoneOAuthService : OAuthService {

    override suspend fun signIn(oAuthRequest: OAuthRequest): Task<AuthResult> {
        throw IllegalStateException("잘 못된 Provider 입니다.")
    }
}
