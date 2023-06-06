package com.lighthouse.beep.auth.service.oauth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.beep.auth.model.OAuthRequest

internal class AnonymousOAuthService : OAuthService {

    override suspend fun signIn(oAuthRequest: OAuthRequest): Task<AuthResult> {
        return Firebase.auth.signInAnonymously()
    }
}
