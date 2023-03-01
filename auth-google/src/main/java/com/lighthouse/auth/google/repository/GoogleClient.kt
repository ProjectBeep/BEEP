package com.lighthouse.auth.google.repository

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.firebase.auth.AuthResult

interface GoogleClient {

    fun signInIntent(): Intent

    suspend fun signIn(result: ActivityResult): Result<AuthResult>

    suspend fun signOut(): Result<Unit>
}
