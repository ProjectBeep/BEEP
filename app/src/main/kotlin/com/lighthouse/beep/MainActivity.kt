package com.lighthouse.beep

import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lighthouse.auth.google.GoogleClient
import com.lighthouse.auth.google.local.LocalGoogleClient
import com.lighthouse.beep.auth.kakao.KakaoClient
import com.lighthouse.beep.auth.kakao.local.LocalKakaoClient
import com.lighthouse.beep.auth.naver.NaverClient
import com.lighthouse.beep.auth.naver.local.LocalNaverClient
import com.lighthouse.beep.domain.monitor.NetworkMonitor
import com.lighthouse.beep.theme.BeepTheme
import com.lighthouse.beep.ui.BeepApp
import com.lighthouse.beep.ui.feature.login.page.login.IntroScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var googleClient: GoogleClient

    @Inject
    lateinit var kakaoClient: KakaoClient

    @Inject
    lateinit var naverClient: NaverClient

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            viewModel.isInit.value
        }
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
                CompositionLocalProvider(
                    LocalGoogleClient provides googleClient,
                    LocalKakaoClient provides kakaoClient,
                    LocalNaverClient provides naverClient,
                ) {
                    BeepApp(
                        windowSizeClass = calculateWindowSizeClass(activity = this),
                        networkMonitor = networkMonitor,
                        startDestination = viewModel.topDestination.collectAsState().value,
                    )
                }
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
