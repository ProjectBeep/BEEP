package com.lighthouse.features.intro.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.lighthouse.features.common.navigator.MainNavigator
import com.lighthouse.features.intro.R
import com.lighthouse.features.intro.databinding.ActivityIntroBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    private val viewModel: IntroViewModel by viewModels()

    @Inject
    lateinit var navigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            if (viewModel.isLogin()) {
                navigator.openMain(this@IntroActivity)
                finish()
            } else {
                binding = DataBindingUtil.setContentView(
                    this@IntroActivity,
                    R.layout.activity_intro
                )
            }
        }
    }
}
