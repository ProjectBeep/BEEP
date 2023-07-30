package com.lighthouse.beep.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lighthouse.beep.domain.monitor.NetworkMonitor
import com.lighthouse.beep.navigation.BeepNavHost
import com.lighthouse.beep.navigation.TopLevelDestination

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BeepApp(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    startDestination: TopLevelDestination,
    needLogin: Boolean,
    appState: BeepAppState = rememberBeepAppState(
        windowSizeClass = windowSizeClass,
        networkMonitor = networkMonitor,
    ),
) {
    val snackBarHostState = remember { SnackbarHostState() }

    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    LaunchedEffect(needLogin) {
        if (needLogin) {
            appState.navigateToTopLevelDestination(TopLevelDestination.LOGIN)
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = { BeepSnackBarHost(snackBarHostState) },
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal),
                ),
        ) {
            if (startDestination != TopLevelDestination.NONE) {
                BeepNavHost(
                    navController = appState.navController,
                    appState = appState,
                    startDestination = startDestination,
                )
            }
        }
    }
}
