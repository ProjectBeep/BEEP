package com.lighthouse.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.lighthouse.beep.R
import com.lighthouse.beep.databinding.ActivityMainBinding
import com.lighthouse.features.intro.ui.IntroFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

//    private val messageViewModel: MessageViewModel by viewModels()

//    private val appNavigationViewModel: AppNavigationViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpMessage()
        setUpNavController()
        setUpNavigation()
        setUpIsLogin()

        setUpIntroScreen()
    }

    private fun setUpIntroScreen() {
        supportFragmentManager.commit {
            add(R.id.fcv, IntroFragment())
        }
    }

    private fun setUpMessage() {
//        repeatOnStarted {
//            messageViewModel.messageFlow.collect { event ->
//                when (event) {
//                    is MessageEvent.SnackBar ->
//                        showSnackBar(event.uiText.asString(this).toString())
//
//                    is MessageEvent.Toast ->
//                        showToast(event.uiText.asString(this).toString())
//                }
//            }
//        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setUpNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv) as NavHostFragment
        navController = navHostFragment.navController
        navController.popBackStack()
    }

    private fun setUpNavigation() {
//        repeatOnStarted {
//            appNavigationViewModel.navigation.collect { item ->
//                when (item) {
//                    AppNavigationItem.Popup ->
//                        onBackPressedDispatcher.onBackPressed()
//
//                    AppNavigationItem.UsedGifticon ->
//                        navController.navigate(R.id.used_gifticon_nav_graph)
//
//                    AppNavigationItem.Security ->
//                        navController.navigate(R.id.security_nav_graph)
//
//                    AppNavigationItem.Coffee ->
//                        navController.navigate(R.id.coffee_nav_graph)
//
//                    AppNavigationItem.TermsOfUse ->
//                        navController.navigate(R.id.terms_of_use_nav_graph)
//
//                    AppNavigationItem.PersonalInfoPolicy ->
//                        navController.navigate(R.id.personal_info_policy_nav_graph)
//
//                    AppNavigationItem.OpensourceLicense ->
//                        navController.navigate(R.id.open_source_license_nav_graph)
//                }
//            }
//        }
    }

    private fun setUpIsLogin() {
//        repeatOnStarted {
//            viewModel.isLoginFlow.collect { isLogin ->
//                when (isLogin) {
//                    true -> navController.navigate(R.id.action_global_main_graph)
//                    false -> navController.navigate(R.id.action_global_intro_graph)
//                }
//            }
//        }
    }
}
