package com.lighthouse.beep

import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighthouse.beep.domain.monitor.NetworkMonitor
import com.lighthouse.beep.theme.BeepTheme
import com.lighthouse.beep.ui.BeepApp
import com.lighthouse.beep.ui.page.intro.IntroScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
//        splashScreen.setKeepOnScreenCondition {
//            true
//        }
        splashScreen.setOnExitAnimationListener { splashScreenProvider ->
            val logo = getDrawable(R.drawable.anim_logo) as? AnimatedVectorDrawable
            val iconView = runCatching {
                splashScreenProvider.iconView as? ImageView
            }.getOrNull()
            if (logo == null || iconView == null) {
                splashScreenProvider.remove()
                return@setOnExitAnimationListener
            }

            iconView.setImageDrawable(logo)
            iconView.alpha = 0f
            iconView.animate()
                .alpha(1f)
                .setDuration(300L)
                .withEndAction {
                    logo.start()
                    logo.registerAnimationCallback(object : AnimationCallback() {
                        override fun onAnimationEnd(drawable: Drawable?) {
                            splashScreenProvider.remove()
                        }
                    })
                }.start()
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()

            DisposableEffect(systemUiController, darkTheme) {
                systemUiController.systemBarsDarkContentEnabled = !darkTheme
                onDispose { }
            }

            BeepTheme(
                darkTheme = darkTheme,
            ) {
                BeepApp(
                    windowSizeClass = calculateWindowSizeClass(activity = this),
                    networkMonitor = networkMonitor,
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun MainPreview() {
    BeepTheme {
        IntroScreen()
    }
}
