package com.lighthouse.beep

import android.content.Intent
import android.graphics.drawable.Animatable2
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lighthouse.beep.model.user.AuthProvider
import com.lighthouse.beep.ui.feature.login.page.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    private val onExitAnimationListener = SplashScreen.OnExitAnimationListener { provider ->
        val logo = ActivityCompat.getDrawable(this, R.drawable.anim_logo) as? AnimatedVectorDrawable
        val iconView = runCatching {
            provider.iconView as? ImageView
        }.getOrNull()
        if (logo == null || iconView == null) {
            provider.remove()
            return@OnExitAnimationListener
        }

        iconView.setImageDrawable(logo)
        iconView.alpha = 0f
        iconView.animate()
            .alpha(1f)
            .setDuration(300L)
            .withEndAction {
                logo.start()
                logo.registerAnimationCallback(object : Animatable2.AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        provider.remove()
                        setUpPageNavigate()
                    }
                })
            }.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value is MainUiState.Success
        }
        splashScreen.setOnExitAnimationListener(onExitAnimationListener)
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
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gotoHomePage() {
//        finish()
    }
}