package com.lighthouse.beep.auth.service.oauth

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.lighthouse.beep.auth.model.OAuthRequest

internal interface OAuthService {

    suspend fun signIn(oAuthRequest: OAuthRequest): Task<AuthResult>
}
