package com.lighthouse.beep.auth

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.AuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow

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

    private fun FirebaseUser?.toAuthInfo(): AuthInfo {
        this ?: return AuthInfo(provider = AuthProvider.NONE)

        val provider = runCatching {
            if (isAnonymous) {
                AuthProvider.GUEST
            } else {
                val result = getIdToken(true).result
                val providerName = result.claims["provider"] as? String ?: ""

                AuthProvider.of(providerName)
            }
        }.getOrDefault(AuthProvider.NONE)

        return AuthInfo(
            userUid = uid,
            provider = provider,
            displayName = displayName ?: "",
            email = email ?: "",
            photoUrl = photoUrl,
        )
    }

    private val _authInfoFlow = MutableStateFlow(AuthInfo.Default)
    val authInfoFlow = _authInfoFlow.asStateFlow()

    init {
        FirebaseAuth.AuthStateListener {
            val info = it.currentUser.toAuthInfo()
            _authInfo = info
            _authInfoFlow.value = info
        }
    }

    private var _authInfo: AuthInfo? = null
    val authInfo: AuthInfo
        get() = _authInfo ?: FirebaseAuth.getInstance().currentUser.toAuthInfo().also {
            _authInfo = it
            _authInfoFlow.value = it
        }

    val userUid: String
        get() = authInfo.userUid

    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser
}