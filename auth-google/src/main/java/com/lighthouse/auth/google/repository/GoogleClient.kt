package com.lighthouse.auth.google.repository

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.firebase.auth.AuthResult

interface GoogleClient {

    fun googleSignInIntent(): Intent

    suspend fun googleSignIn(result: ActivityResult): Result<AuthResult>
}
