package com.lighthouse.beep.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val LightDefaultColorScheme = lightColorScheme()

val DarkDefaultColorScheme = darkColorScheme()

@Composable
fun BeepTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkDefaultColorScheme else LightDefaultColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
