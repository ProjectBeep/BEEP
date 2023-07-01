package com.lighthouse.beep.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

val LightDefaultColorScheme = lightColors()

val DarkDefaultColorScheme = darkColors()

@Composable
fun BeepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme

    MaterialTheme(
        colors = colorScheme,
        content = {
            Surface(
                color = colorScheme.surface,
                modifier = Modifier.fillMaxSize(),
            ) {
                content()
            }
        },
    )
}
