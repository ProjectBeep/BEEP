package com.lighthouse.features.intro.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lighthouse.beep.ui.core.binding.viewBindings
import com.lighthouse.beep.ui.core.exts.repeatOnStarted
import com.lighthouse.beep.ui.core.exts.setOnThrottleClickListener
import com.lighthouse.beep.ui.page.intro.R
import com.lighthouse.beep.ui.page.intro.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        animateLogo()
        setUpGoogleAuthEvent()
        setUpSignInLoading()
        setUpBtnGoogleLogin()
        setUpTvGuestSignIn()
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

    private fun setUpBtnGoogleLogin() {
        binding.btnGoogleLogin.setOnThrottleClickListener {
//            authViewModel.startLogin()
//            googleLoginLauncher.launch(googleClient.signInIntent())
        }
    }

    private fun setUpTvGuestSignIn() {
        binding.tvGuestSignin.setOnThrottleClickListener {
//            googleAuthViewModel.startSignIn()
//            googleAuthViewModel.login()
        }
    }
}
