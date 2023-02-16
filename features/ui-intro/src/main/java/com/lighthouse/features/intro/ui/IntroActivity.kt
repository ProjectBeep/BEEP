package com.lighthouse.features.intro.ui

import android.app.Activity
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.features.common.dialog.progress.ProgressDialog
import com.lighthouse.features.common.ext.repeatOnStarted
import com.lighthouse.features.common.ext.show
import com.lighthouse.features.common.navigator.MainNavigator
import com.lighthouse.features.common.utils.throttle.onThrottleClick
import com.lighthouse.features.intro.R
import com.lighthouse.features.intro.databinding.ActivityIntroBinding
import com.lighthouse.features.intro.exception.FailedConnectException
import com.lighthouse.features.intro.exception.FailedLoginException
import com.lighthouse.features.intro.exception.FailedSaveLoginUserException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    private val viewModel: IntroViewModel by viewModels()

    @Inject
    lateinit var navigator: MainNavigator

    private var progressDialog: ProgressDialog? = null

    private val googleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(this, gso)
    }

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                try {
                    signInWithGoogle(task.getResult(ApiException::class.java))
                } catch (e: Exception) {
                    signInFailed(e)
                }
            } else {
                signInFailed(FailedConnectException())
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            if (viewModel.isLogin()) {
                openMainScreen()
            } else {
                openSignInScreen()
            }
        }
    }

    private fun openMainScreen() {
        navigator.openMain(this@IntroActivity)
        finish()
    }

    private fun openSignInScreen() {
        binding = DataBindingUtil.setContentView(
            this@IntroActivity,
            R.layout.activity_intro
        )

        animateLogo()
        setUpLoadingFlow()
        setUpBtnGoogleLogin()
        setUpTvGuestSignIn()
    }

    private fun animateLogo() {
        val drawable = binding.ivLogo.drawable as AnimatedVectorDrawable
        drawable.start()
    }

    private fun setUpLoadingFlow() {
        repeatOnStarted {
            viewModel.loading.collect { loading ->
                if (loading) {
                    if (progressDialog?.isAdded == true) {
                        progressDialog?.dismiss()
                    }
                    progressDialog = ProgressDialog()
                    progressDialog?.show(supportFragmentManager)
                } else {
                    progressDialog?.dismiss()
                }
            }
        }
    }

    private fun setUpBtnGoogleLogin() {
        binding.btnGoogleLogin.onThrottleClick {
            viewModel.setLoading(true)
            loginLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun setUpTvGuestSignIn() {
        binding.tvGuestSignin.onThrottleClick {
            viewModel.setLoading(true)
            signIn()
        }
    }

    private fun signInWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                signIn()
            } else {
                signInFailed(FailedLoginException())
            }
        }
    }

    private fun signIn() {
        lifecycleScope.launch {
            if (viewModel.login().isSuccess) {
                signInSuccess()
            } else {
                signInFailed(FailedSaveLoginUserException())
            }
        }
    }

    private fun signInSuccess() {
        viewModel.setLoading(false)
        navigator.openMain(this)
    }

    private fun signInFailed(e: Exception) {
        viewModel.setLoading(false)
        val message = when (e) {
            is ApiException -> getString(R.string.google_login_failed)
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
