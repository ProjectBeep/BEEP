package com.lighthouse.presentation.ui.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lighthouse.presentation.R
import com.lighthouse.presentation.databinding.FragmentSignInBinding
import com.lighthouse.presentation.ui.common.viewBindings

class SignInFragment : Fragment() {

    private val binding by viewBindings(FragmentSignInBinding::bind)
    private val viewModel: SignInViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private val activityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

                try {
                    val account = task.getResult(ApiException::class.java)
                    account.idToken?.let { signInWithGoogle(it) }
                } catch (e: ApiException) {
                    Snackbar.make(requireView(), getString(R.string.signin_google_fail), Snackbar.LENGTH_SHORT).show()
                }
            } else {
                Snackbar.make(requireView(), getString(R.string.signin_google_connect_fail), Snackbar.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initGoogleLogin()
    }

    private fun initGoogleLogin() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), googleSignInOptions)

        binding.btnGoogleLogin.setOnClickListener {
            activityLauncher.launch(googleSignInClient.signInIntent)
        }
    }

    private fun signInWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Snackbar.make(requireView(), getString(R.string.signin_success), Snackbar.LENGTH_SHORT).show()
            } else {
                Snackbar.make(requireView(), getString(R.string.signin_fail), Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}
