package com.lighthouse.features.intro.ui

import android.app.Activity
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.lighthouse.beep.ui.core.binding.viewBindings
import com.lighthouse.beep.ui.core.exts.repeatOnStarted
import com.lighthouse.beep.ui.core.utils.throttle.OnThrottleClickListener
import com.lighthouse.beep.ui.page.intro.BuildConfig
import com.lighthouse.beep.ui.page.intro.R
import com.lighthouse.beep.ui.page.intro.databinding.FragmentIntroBinding
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLoginState
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@AndroidEntryPoint
class IntroFragment : Fragment(R.layout.fragment_intro) {

    private val binding: FragmentIntroBinding by viewBindings()

//    private val googleLoginLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            lifecycleScope.launch {
//                try {
//                    val credential = googleClient.getAuthCredential(result).getOrThrow()
//                    authViewModel.login(AuthProvider.GOOGLE, credential)
//                } catch (e: Exception) {
//                    authViewModel.endLogin(e)
//                }
//            }
//        }

    private val googleLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                    .getResult(ApiException::class.java)
                Log.d("GOOGLE_LOGIN", "login accessToken : ${account.idToken}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    private val naverLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    Log.d("NAVER_LOGIN", "login accessToken : ${NaverIdLoginSDK.getAccessToken()}")
                }

                Activity.RESULT_CANCELED -> {
                }

                else -> {
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animateLogo()
        setUpGoogleAuthEvent()
        setUpSignInLoading()
        setUpClickEvent()

        NaverIdLoginSDK.initialize(
            requireContext(),
            BuildConfig.NAVER_LOGIN_CLIENT_ID,
            BuildConfig.NAVER_LOGIN_CLIENT_SECRET,
            getString(com.lighthouse.beep.theme.R.string.app_name),
        )
        Log.d("NAVER_LOGIN", "accessToken : ${NaverIdLoginSDK.getAccessToken()}")
    }

    private fun animateLogo() {
        val drawable = binding.ivLogo.drawable as AnimatedVectorDrawable
        drawable.start()
    }

    private fun setUpGoogleAuthEvent() {
        repeatOnStarted {
//            authViewModel.eventFlow.collect { event ->

//                messageViewModel.sendMessage(event)
//            }
        }
    }

    private fun setUpSignInLoading() {
        repeatOnStarted {
//            authViewModel.loginLoading.collect { loading ->
//                val progressDialog =
//                    childFragmentManager.findDialogFragment(ProgressDialog::class)
//                        ?: ProgressDialog()
//                if (loading) {
//                    if (progressDialog.isAdded) {
//                        progressDialog.dismiss()
//                    }
//                    progressDialog.show(childFragmentManager)
//                } else {
//                    progressDialog.dismiss()
//                }
//            }
        }
    }

    fun authFirebaseWithGoogle() {
        val retrofit = Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor())
                    .build(),
            )
            .baseUrl(BuildConfig.FIREBASE_GOOGLE_AUTH_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private fun setUpClickEvent() {
        binding.btnGoogleLogin.setOnClickListener(
            OnThrottleClickListener(viewLifecycleOwner) {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
                    .requestEmail()
                    .build()
                val client = GoogleSignIn.getClient(requireActivity(), gso)
                googleLoginLauncher.launch(client.signInIntent)
            },
        )

        binding.btnKakaoLogin.setOnClickListener(
            OnThrottleClickListener(viewLifecycleOwner) {
                KakaoSdk.init(requireContext(), "b8bffbe2e013b8cc065cef5b395fe23c")
                if (AuthApiClient.instance.hasToken()) {
                    UserApiClient.instance.accessTokenInfo { token, error ->
                        if (error != null) {
                            if (error is KakaoSdkError && error.isInvalidTokenError()) {
                                Log.d("AUTH_TEST", "kakao auth need login")
                            } else {
                                Log.d("AUTH_TEST", "kakao auth error")
                            }
                        } else {
                            val accessToken =
                                AuthApiClient.instance.tokenManagerProvider.manager.getToken()?.accessToken
                            Log.d("AUTH_TEST", "kakao success : $accessToken")
                        }
                    }
                } else {
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                        UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                            if (error != null) {
                                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                    Log.d("AUTH_TEST", "kakao talk login cancel : $error")
                                } else {
                                    Log.d("AUTH_TEST", "kakao talk error : $error")
                                }
                            } else {
                                Log.d("AUTH_TEST", "kakao talk token : $token")
                            }
                        }
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(requireContext()) { token, error ->
                            if (error != null) {
                                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                                    Log.d("AUTH_TEST", "kakao account login cancel : $error")
                                } else {
                                    Log.d("AUTH_TEST", "kakao account error : $error")
                                }
                            } else {
                                Log.d("AUTH_TEST", "kakao account token : $token")
                            }
                        }
                    }
                }
            },
        )

        binding.btnNaverLogin.setOnClickListener(
            OnThrottleClickListener(viewLifecycleOwner) {
                NaverIdLoginSDK.initialize(
                    requireContext(),
                    BuildConfig.NAVER_LOGIN_CLIENT_ID,
                    BuildConfig.NAVER_LOGIN_CLIENT_SECRET,
                    getString(com.lighthouse.beep.theme.R.string.app_name),
                )
                if (NaverIdLoginSDK.getState() == NidOAuthLoginState.OK) {
                    NaverIdLoginSDK.getAccessToken()
                } else {
                    NaverIdLoginSDK.authenticate(requireContext(), naverLoginLauncher)
                }
            },
        )

        binding.tvGuestSignin.setOnClickListener(
            OnThrottleClickListener(viewLifecycleOwner) {
            },
        )
    }
}
