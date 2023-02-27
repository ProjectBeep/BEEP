package com.lighthouse.features.intro.ui

import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.lighthouse.auth.google.exception.FailedApiException
import com.lighthouse.auth.google.exception.FailedConnectException
import com.lighthouse.auth.google.exception.FailedLoginException
import com.lighthouse.auth.google.repository.GoogleClient
import com.lighthouse.features.common.dialog.progress.ProgressDialog
import com.lighthouse.features.common.ext.repeatOnStarted
import com.lighthouse.features.common.ext.show
import com.lighthouse.features.common.utils.throttle.onThrottleClick
import com.lighthouse.features.intro.R
import com.lighthouse.features.intro.databinding.FragmentIntroBinding
import com.lighthouse.features.intro.exception.FailedSaveLoginUserException
import com.lighthouse.features.intro.model.SignInState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroFragment : Fragment(R.layout.fragment_intro) {

    private lateinit var binding: FragmentIntroBinding

    private val viewModel: IntroViewModel by viewModels()

    @Inject
    lateinit var googleClient: GoogleClient

    private var progressDialog: ProgressDialog? = null

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            lifecycleScope.launch {
                val exception = googleClient.googleSignIn(result).exceptionOrNull()
                if (exception != null) {
                    viewModel.setState(SignInState.Failed(exception))
                } else {
                    signIn()
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animateLogo()
        setUpSignInStateFlow()
        setUpBtnGoogleLogin()
        setUpTvGuestSignIn()
    }

    private fun animateLogo() {
        val drawable = binding.ivLogo.drawable as AnimatedVectorDrawable
        drawable.start()
    }

    private fun setUpSignInStateFlow() {
        repeatOnStarted {
            viewModel.signInState.collect { state ->
                if (state == SignInState.Loading) {
                    if (progressDialog?.isAdded == true) {
                        progressDialog?.dismiss()
                    }
                    progressDialog = ProgressDialog()
                    progressDialog?.show(childFragmentManager)
                } else {
                    progressDialog?.dismiss()
                }

                when (state) {
                    is SignInState.Success -> signInSuccess()
                    is SignInState.Failed -> signInFailed(state.e)
                    else -> {}
                }
            }
        }
    }

    private fun setUpBtnGoogleLogin() {
        binding.btnGoogleLogin.onThrottleClick {
            viewModel.setState(SignInState.Loading)
            loginLauncher.launch(googleClient.googleSignInIntent())
        }
    }

    private fun setUpTvGuestSignIn() {
        binding.tvGuestSignin.onThrottleClick {
            viewModel.setState(SignInState.Loading)
            signIn()
        }
    }

    private fun signIn() {
        lifecycleScope.launch {
            val state = if (viewModel.login().isSuccess) {
                SignInState.Success
            } else {
                SignInState.Failed(FailedSaveLoginUserException())
            }
            viewModel.setState(state)
        }
    }

    private fun signInSuccess() {
        val message = getString(R.string.login_success)
        showSnackBar(message)
    }

    private fun signInFailed(e: Exception) {
        val message = when (e) {
            is FailedApiException -> getString(R.string.google_login_failed)
            is FailedLoginException -> getString(R.string.login_failed)
            is FailedSaveLoginUserException -> getString(R.string.error_save_login_user)
            is FailedConnectException -> getString(R.string.google_connect_fail)
            else -> getString(R.string.error_unknown)
        }
        showSnackBar(message)
    }

    private fun showSnackBar(string: String) {
        Snackbar.make(binding.root, string, Snackbar.LENGTH_SHORT).show()
    }
}
