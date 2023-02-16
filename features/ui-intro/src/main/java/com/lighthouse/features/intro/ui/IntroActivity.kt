package com.lighthouse.features.intro.ui

import android.app.Activity
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
import com.lighthouse.features.common.navigator.MainNavigator
import com.lighthouse.features.common.utils.throttle.onThrottleClick
import com.lighthouse.features.intro.R
import com.lighthouse.features.intro.databinding.ActivityIntroBinding
import com.lighthouse.features.intro.exception.NotFoundEmailException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    private val viewModel: IntroViewModel by viewModels()

    @Inject
    lateinit var navigator: MainNavigator

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
                } catch (e: ApiException) {
                    showSnackBar(getString(R.string.google_login_failed))
                } catch (e: NotFoundEmailException) {
                    showSnackBar(getString(R.string.error_not_found_email))
                }
            } else {
                showSnackBar(getString(R.string.google_connect_fail))
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

        initBtnGoogleLogin()
    }

    private fun initBtnGoogleLogin() {
        binding.btnGoogleLogin.onThrottleClick {
            loginLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun signInWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                signIn()
            } else {
                showSnackBar(getString(R.string.login_failed))
            }
        }
    }

    private fun signIn() {
        lifecycleScope.launch {
            if (viewModel.login().isSuccess) {
                navigator.openMain(this@IntroActivity)
            } else {
                showSnackBar(getString(R.string.error_save_login_user))
            }
        }
    }

    private fun showSnackBar(string: String) {
        Snackbar.make(binding.root, string, Snackbar.LENGTH_SHORT).show()
    }
}
