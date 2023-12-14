package com.lighthouse.beep

import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import com.lighthouse.beep.theme.R
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lighthouse.beep.databinding.ActivityMainBinding
import com.lighthouse.beep.model.user.AuthProvider
import com.lighthouse.beep.navs.ActivityNavItem
import com.lighthouse.beep.navs.AppNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var navigator: AppNavigator

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            viewModel.uiState.value is MainUiState.Success
        }
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window?.statusBarColor = getColor(R.color.beep_pink)

        val logo = binding.imageLogo.drawable as? AnimatedVectorDrawable
        logo?.registerAnimationCallback(object: Animatable2.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                setUpPageNavigate()
            }
        })
        logo?.start()
    }

    private fun setUpPageNavigate() {
        lifecycleScope.launch {
            val uiState = viewModel.uiState.filterIsInstance<MainUiState.Success>().first()
            val provider = uiState.userConfig.authInfo.provider
            if (provider == AuthProvider.NONE) {
                gotoLoginPage()
            } else {
                gotoHomePage()
            }
        }
    }

    private fun gotoLoginPage() {
        val intent = navigator.getIntent(this, ActivityNavItem.Login)
        startActivity(intent)
        finish()
    }

    private fun gotoHomePage() {
        val intent = navigator.getIntent(this, ActivityNavItem.Home)
        startActivity(intent)
        finish()
    }
}