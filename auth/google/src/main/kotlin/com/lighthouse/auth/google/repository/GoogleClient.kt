package com.lighthouse.auth.google.repository

import android.content.Intent
import androidx.activity.result.ActivityResult
import com.google.firebase.auth.AuthCredential

interface GoogleClient {

    fun signInIntent(): Intent

    suspend fun getAuthCredential(result: ActivityResult): Result<AuthCredential>
}
