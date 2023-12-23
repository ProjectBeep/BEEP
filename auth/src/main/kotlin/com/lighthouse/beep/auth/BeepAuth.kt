package com.lighthouse.beep.auth

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lighthouse.beep.auth.mapper.toAuthInfo
import com.lighthouse.beep.model.user.AuthInfo
import com.lighthouse.beep.model.user.AuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

object BeepAuth {

    private val authScope = CoroutineScope(Dispatchers.IO)

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

    init {
        FirebaseAuth.getInstance().addAuthStateListener {
            authScope.launch {
                val info = it.currentUser.toAuthInfo()
                authInfo = info
                _authInfoFlow.value = info
            }
        }
    }

    var authInfo: AuthInfo? = null
        private set

    val userUid: String
        get() = authInfo?.userUid ?: ""

    val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser
}