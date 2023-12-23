package com.lighthouse.beep.auth.client

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import com.lighthouse.beep.auth.BuildConfig
import com.lighthouse.beep.theme.R as ThemeR
import com.lighthouse.beep.auth.model.OAuthTokenResult
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLoginState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class NaverClient(context: Context) {

    init {
        NaverIdLoginSDK.initialize(
            context,
            BuildConfig.NAVER_LOGIN_CLIENT_ID,
            BuildConfig.NAVER_LOGIN_CLIENT_SECRET,
            context.resources.getString(ThemeR.string.app_name),
        )
    }

    fun authenticate(activity: Activity, launcher: ActivityResultLauncher<Intent>) {
        NaverIdLoginSDK.authenticate(activity, launcher)
    }

    fun getAccessToken(): OAuthTokenResult {
        if (NaverIdLoginSDK.getState() == NidOAuthLoginState.OK) {
            val token = NaverIdLoginSDK.getAccessToken()
            if (token.isNullOrEmpty()) {
                throw NullPointerException("Token is Null!")
            } else {
                return OAuthTokenResult.Success(token)
            }
        } else {
            throw IllegalStateException("네이버에 로그인 되지 않았습니다")
        }
    }

    fun getAccessToken(result: ActivityResult): OAuthTokenResult {
        return when (result.resultCode) {
            Activity.RESULT_OK -> {
                val token = NaverIdLoginSDK.getAccessToken()
                if (token.isNullOrEmpty()) {
                    throw NullPointerException("Token is Null!")
                } else {
                    OAuthTokenResult.Success(token)
                }
            }

            Activity.RESULT_CANCELED -> {
                OAuthTokenResult.Canceled(
                    NaverIdLoginSDK.getLastErrorCode().code,
                    NaverIdLoginSDK.getLastErrorDescription() ?: "",
                )
            }

            else -> {
                throw IllegalStateException("네이버 로그인에 실패 했습니다.")
            }
        }
    }

    fun signOut() {
        NaverIdLoginSDK.logout()
    }
}
