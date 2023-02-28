package com.lighthouse.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.lighthouse.beep.R
import com.lighthouse.beep.databinding.ActivityMainBinding
import com.lighthouse.features.common.ext.repeatOnStarted
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setUpIsLogin()
    }

    private fun setUpIsLogin() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv) as NavHostFragment
        val navController = navHostFragment.navController
        navController.popBackStack()
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
