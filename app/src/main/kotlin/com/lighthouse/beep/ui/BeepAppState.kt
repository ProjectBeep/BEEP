package com.lighthouse.beep.ui

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.lighthouse.beep.domain.monitor.NetworkMonitor
import com.lighthouse.beep.navigation.TopLevelDestination
import com.lighthouse.beep.ui.feature.login.navigation.navigateToLogin
import com.lighthouse.beep.ui.feature.main.navigation.navigateToMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@Stable
class BeepAppState(
    val windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val shouldShowBottomBar: Boolean
        get() = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact

    val shouldShowNavRail: Boolean
        get() = windowSizeClass.widthSizeClass != WindowWidthSizeClass.Compact

    val isOffline = networkMonitor.isAvailable
        .map(Boolean::not)
        .stateIn(coroutineScope, SharingStarted.Eagerly, false)

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val navOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
            launchSingleTop = true
        }

        when (topLevelDestination) {
            TopLevelDestination.LOGIN -> navController.navigateToLogin(navOptions)
            TopLevelDestination.MAIN -> navController.navigateToMain(navOptions)
            TopLevelDestination.NONE -> {}
        }
    }

    fun onBackClick() {
        navController.popBackStack()
    }
}

@Composable
fun rememberBeepAppState(
    windowSizeClass: WindowSizeClass,
    networkMonitor: NetworkMonitor,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    navController: NavHostController = rememberNavController(),
): BeepAppState {
    return remember(
        windowSizeClass,
        networkMonitor,
        coroutineScope,
        navController,
    ) {
        BeepAppState(windowSizeClass, networkMonitor, navController, coroutineScope)
    }
}
