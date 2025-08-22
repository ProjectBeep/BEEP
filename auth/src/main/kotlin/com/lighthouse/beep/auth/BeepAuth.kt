package com.lighthouse.beep.auth

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.UserProfileChangeRequest
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.AuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

object BeepAuth {

    fun getSignInIntent(context: Context, provider: AuthProvider): Intent {
        return AuthParam.SignIn(provider).getIntent(context)
    }

    fun getSignOutIntent(context: Context): Intent {
        return AuthParam.SignOut.getIntent(context)
    }

    fun getWithdrawalIntent(context: Context): Intent {
        return AuthParam.Withdrawal.getIntent(context)
    }

    private val _authInfoFlow = MutableStateFlow<AuthInfo?>(null)
    val authInfoFlow = _authInfoFlow.asStateFlow()

    val currentUid = authInfoFlow.filterNotNull().map { it.userUid }

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            val currentUser = it.currentUser
            if (currentUser == null) {
                _authInfoFlow.value = AuthInfo.Default
            } else {
                currentUser.getIdToken(true).addOnSuccessListener { result ->
                    val info = getAuthInfo(currentUser, result)
                    _authInfoFlow.value = info
                }
            }
        }
    }

    private fun getAuthInfo(
        user: FirebaseUser,
        result: GetTokenResult
    ): AuthInfo {
        val provider = runCatching {
            if (user.isAnonymous) {
                AuthProvider.GUEST
            } else {
                val providerName = result.claims["provider"] as? String ?: ""
                AuthProvider.of(providerName)
            }
        }.getOrDefault(AuthProvider.NONE)

        return AuthInfo(
            userUid = user.uid,
            provider = provider,
            displayName = user.displayName ?: "",
            email = user.email ?: "",
            photoUrl = user.photoUrl,
        )
    }

    suspend fun updateProfile(request: UserProfileChangeRequest) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            currentUser.updateProfile(request).await()
            val result = currentUser.getIdToken(false).await()
            _authInfoFlow.value = getAuthInfo(currentUser, result)
        }
    }

    val authInfo: AuthInfo?
        get() = authInfoFlow.value

    val authProvider: AuthProvider
        get() = authInfo?.provider ?: AuthProvider.NONE

    val userUid: String
        get() = authInfo?.userUid ?: ""
}