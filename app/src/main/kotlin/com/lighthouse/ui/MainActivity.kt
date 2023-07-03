package com.lighthouse.ui

import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.lighthouse.beep.R
import com.lighthouse.beep.theme.BeepTheme
import com.lighthouse.beep.ui.page.intro.IntroScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
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

        setContent {
            BeepTheme {
                IntroScreen()
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
