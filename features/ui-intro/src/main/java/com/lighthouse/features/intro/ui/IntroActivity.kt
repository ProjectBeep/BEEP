package com.lighthouse.features.intro.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import com.lighthouse.features.common.navigator.MainNavigator
import com.lighthouse.features.intro.R
import com.lighthouse.features.intro.databinding.ActivityIntroBinding
import javax.inject.Inject

class IntroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntroBinding

    private val viewModel: IntroViewModel by viewModels()

    @Inject
    lateinit var navigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_intro)
    }
}
