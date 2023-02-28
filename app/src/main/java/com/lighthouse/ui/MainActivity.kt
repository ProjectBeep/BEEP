package com.lighthouse.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.lighthouse.beep.R
import com.lighthouse.beep.databinding.ActivityMainBinding
import com.lighthouse.features.common.ext.repeatOnStarted
import com.lighthouse.features.common.model.NavigationItem
import com.lighthouse.features.common.navigator.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private val navigationViewModel: NavigationViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpNavController()
        setUpNavigation()
        setUpIsLogin()
    }

    private fun setUpNavController() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv) as NavHostFragment
        navController = navHostFragment.navController
        navController.popBackStack()
    }

    private fun setUpNavigation() {
        repeatOnStarted {
            navigationViewModel.navigation.collect { item ->
                when (item) {
                    NavigationItem.Popup -> navController.popBackStack()
                    NavigationItem.UsedGifticon -> navController.navigate(R.id.used_gifticon_nav_graph)
                    NavigationItem.Security -> navController.navigate(R.id.security_nav_graph)
                    NavigationItem.Coffee -> navController.navigate(R.id.coffee_nav_graph)
                    NavigationItem.TermsOfUse -> navController.navigate(R.id.terms_of_use_nav_graph)
                    NavigationItem.PersonalInfoPolicy -> navController.navigate(R.id.personal_info_policy_nav_graph)
                    NavigationItem.OpensourceLicense -> navController.navigate(R.id.open_source_license_nav_graph)
                }
            }
        }
    }

    private fun setUpIsLogin() {
        repeatOnStarted {
            viewModel.isLogin().collect { isLogin ->
                if (isLogin) {
                    navController.navigate(R.id.action_global_main_graph)
                } else {
                    navController.navigate(R.id.action_global_intro_graph)
                }
            }
        }
    }
}
