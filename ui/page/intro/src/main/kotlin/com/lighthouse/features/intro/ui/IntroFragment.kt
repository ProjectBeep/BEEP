package com.lighthouse.features.intro.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lighthouse.auth.google.repository.GoogleClient
import com.lighthouse.beep.model.auth.AuthProvider
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.common.dialog.progress.ProgressDialog
import com.lighthouse.features.common.ext.repeatOnStarted
import com.lighthouse.features.common.ext.show
import com.lighthouse.features.common.ui.AuthViewModel
import com.lighthouse.features.common.ui.MessageViewModel
import com.lighthouse.features.common.utils.throttle.onThrottleClick
import com.lighthouse.features.intro.R
import com.lighthouse.features.intro.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroFragment : Fragment(R.layout.fragment_intro) {

    private val binding by viewBindings<FragmentIntroBinding>()

    private val messageViewModel: MessageViewModel by activityViewModels()

    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var googleClient: GoogleClient

    private var progressDialog: ProgressDialog? = null

    private val googleLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            lifecycleScope.launch {
                try {
                    val credential = googleClient.getAuthCredential(result).getOrThrow()
                    authViewModel.login(AuthProvider.GOOGLE, credential)
                } catch (e: Exception) {
                    authViewModel.endLogin(e)
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            authViewModel.eventFlow.collect { event ->
                messageViewModel.sendMessage(event)
            }
        }
    }

    private fun setUpSignInLoading() {
        repeatOnStarted {
            authViewModel.loginLoading.collect { loading ->
                if (loading) {
                    if (progressDialog?.isAdded == true) {
                        progressDialog?.dismiss()
                    }
                    progressDialog = ProgressDialog()
                    progressDialog?.show(childFragmentManager)
                } else {
                    progressDialog?.dismiss()
                }
            }
        }
    }

    private fun setUpBtnGoogleLogin() {
        binding.btnGoogleLogin.onThrottleClick {
            authViewModel.startLogin()
            googleLoginLauncher.launch(googleClient.signInIntent())
        }
    }

    private fun setUpTvGuestSignIn() {
        binding.tvGuestSignin.onThrottleClick {
//            googleAuthViewModel.startSignIn()
//            googleAuthViewModel.login()
        }
    }
}
