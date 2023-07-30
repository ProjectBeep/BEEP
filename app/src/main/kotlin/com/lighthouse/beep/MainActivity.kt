package com.lighthouse.beep

import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lighthouse.auth.google.GoogleClient
import com.lighthouse.auth.google.local.LocalGoogleClient
import com.lighthouse.beep.auth.kakao.KakaoClient
import com.lighthouse.beep.auth.kakao.local.LocalKakaoClient
import com.lighthouse.beep.auth.naver.NaverClient
import com.lighthouse.beep.auth.naver.local.LocalNaverClient
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.domain.monitor.NetworkMonitor
import com.lighthouse.beep.model.user.AuthProvider
import com.lighthouse.beep.model.user.ThemeOption
import com.lighthouse.beep.navigation.TopLevelDestination
import com.lighthouse.beep.theme.BeepTheme
import com.lighthouse.beep.ui.BeepApp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
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

    private val onExitAnimationListener = SplashScreen.OnExitAnimationListener { provider ->
        val logo = getDrawable(R.drawable.anim_logo) as? AnimatedVectorDrawable
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
                logo.registerAnimationCallback(object : AnimationCallback() {
                    override fun onAnimationEnd(drawable: Drawable?) {
                        provider.remove()
                    }
                })
            }.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState by mutableStateOf<MainUiState>(MainUiState.Loading)
        var startDestination by mutableStateOf(TopLevelDestination.NONE)
        var needLogin by mutableStateOf(false)

        repeatOnStarted {
            viewModel.uiState.collect { uiState = it }
        }

        repeatOnStarted {
            viewModel.uiState.detectLoginRequirement().collect {
                needLogin = it.userConfig.authInfo.provider == AuthProvider.NONE
            }
        }

        lifecycleScope.launch {
            val state = viewModel.uiState.filterIsInstance<MainUiState.Success>().first()
            startDestination = if (state.userConfig.authInfo.provider == AuthProvider.NONE) {
                TopLevelDestination.LOGIN
            } else {
                TopLevelDestination.MAIN
            }
        }

        splashScreen.setKeepOnScreenCondition {
            uiState == MainUiState.Loading
        }
        splashScreen.setOnExitAnimationListener(onExitAnimationListener)

        enableEdgeToEdge()

        setContent {
            val darkTheme = shouldUseDarkTheme(uiState = uiState)

            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.TRANSPARENT,
                        android.graphics.Color.TRANSPARENT,
                    ) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                        android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b),
                    ) { darkTheme },
                )
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
                        startDestination = startDestination,
                        needLogin = needLogin,
                    )
                }
            }
        }
    }

    @Composable
    private fun shouldUseDarkTheme(
        uiState: MainUiState,
    ): Boolean = when (uiState) {
        is MainUiState.Loading -> isSystemInDarkTheme()
        is MainUiState.Success -> when (uiState.userConfig.themeOption) {
            ThemeOption.SYSTEM -> isSystemInDarkTheme()
            ThemeOption.LIGHT -> false
            ThemeOption.DARK -> true
        }
    }
}
