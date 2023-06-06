package com.lighthouse.beep.auth.naver

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLoginState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NaverClient @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    init {
        NaverIdLoginSDK.initialize(
            context,
            BuildConfig.NAVER_LOGIN_CLIENT_ID,
            BuildConfig.NAVER_LOGIN_CLIENT_SECRET,
            "",
        )
    }

    fun requestAccessToken(launcher: ActivityResultLauncher<Intent>, block: (String) -> Unit) {
        if (NaverIdLoginSDK.getState() == NidOAuthLoginState.OK) {
            val token = NaverIdLoginSDK.getAccessToken()
            if (token != null) {
                block(token)
            } else {
                NaverIdLoginSDK.authenticate(context, launcher)
            }
        } else {
            NaverIdLoginSDK.authenticate(context, launcher)
        }
    }

    fun getAccessToken(result: ActivityResult): NaverTokenResult {
        return when (result.resultCode) {
            Activity.RESULT_OK -> {
                val token = NaverIdLoginSDK.getAccessToken()
                if (token != null) {
                    NaverTokenResult.Success(token)
                } else {
                    NaverTokenResult.Failed(NullPointerException("Token is Null!"))
                }
            }

            Activity.RESULT_CANCELED -> {
                NaverTokenResult.Canceled
            }

            else -> {
                NaverTokenResult.Failed(IllegalStateException("네이버 로그인에 실패 했습니다."))
            }
        }
    }

    fun signOut() {
        NaverIdLoginSDK.logout()
    }
}
