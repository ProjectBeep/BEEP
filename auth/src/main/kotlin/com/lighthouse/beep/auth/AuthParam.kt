package com.lighthouse.beep.auth

import android.content.Context
import android.content.Intent
import com.lighthouse.beep.auth.model.AuthAction
import com.lighthouse.beep.model.user.AuthProvider

internal sealed class AuthParam(
    private val authAction: AuthAction,
) {

    companion object {
        private const val KEY_AUTH_ACTION = "Key.AuthAction"

        private fun getAuthAction(intent: Intent?): AuthAction {
            val name = intent?.getStringExtra(KEY_AUTH_ACTION) ?: ""
            return AuthAction.of(name)
        }

        fun getParam(intent: Intent?): AuthParam {
            return when(getAuthAction(intent)) {
                AuthAction.NONE -> None
                AuthAction.SIGN_IN -> {
                    val provider = SignIn.getAuthProvider(intent)
                    SignIn(provider)
                }
                AuthAction.SIGN_OUT -> SignOut
                AuthAction.WITHDRAWAL -> Withdrawal
            }
        }
    }

    fun getIntent(context: Context): Intent {
        return Intent(context, AuthActivity::class.java).apply {
            putExtra(KEY_AUTH_ACTION, authAction.name)
            applyIntent(this)
        }
    }

    protected open fun applyIntent(intent: Intent): Intent = intent

    data object None : AuthParam(AuthAction.NONE)

    data class SignIn(val provider: AuthProvider) : AuthParam(AuthAction.SIGN_IN) {

        companion object {
            private const val KEY_AUTH_PROVIDER = "Key.AuthProvider"

            fun getAuthProvider(intent: Intent?): AuthProvider {
                val firebaseName = intent?.getStringExtra(KEY_AUTH_PROVIDER) ?: ""
                return AuthProvider.of(firebaseName)
            }
        }

        override fun applyIntent(intent: Intent): Intent = intent.apply {
            putExtra(KEY_AUTH_PROVIDER, provider.firebaseName)
        }
    }

    data object SignOut : AuthParam(AuthAction.SIGN_OUT)

    data object Withdrawal : AuthParam(AuthAction.WITHDRAWAL)
}