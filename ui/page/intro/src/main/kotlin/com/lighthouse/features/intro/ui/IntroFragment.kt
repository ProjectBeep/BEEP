package com.lighthouse.features.intro.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApiClient
import com.lighthouse.auth.google.GoogleClient
import com.lighthouse.auth.google.GoogleTokenResult
import com.lighthouse.beep.auth.naver.NaverClient
import com.lighthouse.beep.auth.naver.NaverTokenResult
import com.lighthouse.beep.ui.core.binding.viewBindings
import com.lighthouse.beep.ui.core.exts.repeatOnStarted
import com.lighthouse.beep.ui.core.utils.throttle.OnThrottleClickListener
import com.lighthouse.beep.ui.page.intro.R
import com.lighthouse.beep.ui.page.intro.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

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
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            viewLifecycleOwner.lifecycleScope.launch {
                val result = googleClient.getAccessToken(activityResult)
                when (result) {
                    is GoogleTokenResult.Success -> {
                        val idToken = result.idToken
                    }

                    is GoogleTokenResult.Canceled -> {}
                    is GoogleTokenResult.Failed -> {}
                }
            }
        }

    private val naverLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            val result = naverClient.getAccessToken(activityResult)
            when (result) {
                is NaverTokenResult.Success -> {
                    val accessToken = result.accessToken
                }

                is NaverTokenResult.Canceled -> {}
                is NaverTokenResult.Failed -> {}
            }
        }

    @Inject
    lateinit var googleClient: GoogleClient

    @Inject
    lateinit var naverClient: NaverClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animateLogo()
        setUpSignInLoading()
        setUpClickEvent()
    }

    private fun animateLogo() {
        val drawable = binding.ivLogo.drawable as AnimatedVectorDrawable
        drawable.start()
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

    fun authFirebaseWithKakao() {
//        FirebaseAuthWithKakaoApi().request(
//            FirebaseAuthWithKakaoApi.Param(
//                token = ""
//            )
//        )
    }

    fun authFirebaseWithGoogle() {
//        val retrofit = Retrofit.Builder()
//            .client(
//                OkHttpClient.Builder()
//                    .addInterceptor(HttpLoggingInterceptor())
//                    .build(),
//            )
//            .baseUrl(BuildConfig.FIREBASE_GOOGLE_AUTH_URL)
//            .addConverterFactory(MoshiConverterFactory.create())
//            .build()
    }

    private fun setUpClickEvent() {
        binding.btnGoogleLogin.setOnClickListener(
            OnThrottleClickListener(viewLifecycleOwner) {
                googleLoginLauncher.launch(googleClient.signInIntent)
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
                naverClient.requestAccessToken(naverLoginLauncher) { accessToken ->
                }
            },
        )

        binding.tvGuestSignin.setOnClickListener(
            OnThrottleClickListener(viewLifecycleOwner) {
            },
        )
    }
}
