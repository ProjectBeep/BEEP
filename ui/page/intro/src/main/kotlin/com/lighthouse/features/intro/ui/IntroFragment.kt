package com.lighthouse.features.intro.ui

// import android.graphics.drawable.AnimatedVectorDrawable
// import android.os.Bundle
// import android.util.Log
// import android.view.View
// import androidx.activity.result.contract.ActivityResultContracts
// import androidx.fragment.app.Fragment
// import androidx.lifecycle.lifecycleScope
// import com.lighthouse.auth.google.GoogleClient
// import com.lighthouse.auth.google.GoogleTokenResult
// import com.lighthouse.beep.auth.kakao.KakaoClient
// import com.lighthouse.beep.auth.kakao.KakaoTokenResult
// import com.lighthouse.beep.auth.naver.NaverClient
// import com.lighthouse.beep.auth.naver.NaverTokenResult
// import com.lighthouse.beep.core.ui.binding.viewBindings
// import com.lighthouse.beep.core.ui.exts.repeatOnStarted
// import com.lighthouse.beep.core.ui.utils.throttle.OnThrottleClickListener
// import com.lighthouse.beep.ui.page.intro.R
// import dagger.hilt.android.AndroidEntryPoint
// import kotlinx.coroutines.launch
// import javax.inject.Inject

// @AndroidEntryPoint
// class IntroFragment : Fragment(R.layout.fragment_intro) {
//
//    private val binding: FragmentIntroBinding by viewBindings()
//
//    private val googleLoginLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
//            viewLifecycleOwner.lifecycleScope.launch {
//                val result = googleClient.getAccessToken(activityResult)
//                when (result) {
//                    is GoogleTokenResult.Success -> {
//                        val idToken = result.idToken
//                        Log.d("AUTH_TEST", "google accessToken with login : $idToken")
//                    }
//
//                    is GoogleTokenResult.Canceled -> {}
//                    is GoogleTokenResult.Failed -> {}
//                }
//            }
//        }
//
//    private val naverLoginLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
//            val result = naverClient.getAccessToken(activityResult)
//            when (result) {
//                is NaverTokenResult.Success -> {
//                    val accessToken = result.accessToken
//                    Log.d("AUTH_TEST", "naver accessToken with login : $accessToken")
//                }
//
//                is NaverTokenResult.Canceled -> {}
//                is NaverTokenResult.Failed -> {}
//            }
//        }
//
//    @Inject
//    lateinit var googleClient: GoogleClient
//
//    @Inject
//    lateinit var kakaoClient: KakaoClient
//
//    @Inject
//    lateinit var naverClient: NaverClient
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        animateLogo()
//        setUpSignInLoading()
//        setUpClickEvent()
//    }
//
//    private fun animateLogo() {
//        val drawable = binding.ivLogo.drawable as AnimatedVectorDrawable
//        drawable.start()
//    }
//
//    private fun setUpSignInLoading() {
//        repeatOnStarted {
//        }
//    }
//
//    private fun setUpClickEvent() {
//        binding.btnGoogleLogin.setOnClickListener(
//            OnThrottleClickListener(viewLifecycleOwner) {
//                googleLoginLauncher.launch(googleClient.signInIntent)
//            },
//        )
//
//        binding.btnKakaoLogin.setOnClickListener(
//            OnThrottleClickListener(viewLifecycleOwner) {
//                val result = kakaoClient.getAccessToken(requireContext())
//                when (result) {
//                    is KakaoTokenResult.Success -> {
//                        val accessToken = result.accessToken
//                        Log.d("AUTH_TEST", "kakao accessToken : $accessToken")
//                    }
//
//                    is KakaoTokenResult.Canceled -> {
//                        Log.d("AUTH_TEST", "kakao canceled")
//                    }
//
//                    is KakaoTokenResult.Failed -> {
//                        Log.d("AUTH_TEST", "kakao failed : ${result.throwable}")
//                    }
//                }
//            },
//        )
//
//        binding.btnNaverLogin.setOnClickListener(
//            OnThrottleClickListener(viewLifecycleOwner) {
//                naverClient.requestAccessToken(naverLoginLauncher) { accessToken ->
//                    Log.d("AUTH_TEST", "naver accessToken : $accessToken")
//                }
//            },
//        )
//
//        binding.tvGuestSignin.setOnClickListener(
//            OnThrottleClickListener(viewLifecycleOwner) {
//            },
//        )
//    }
// }
