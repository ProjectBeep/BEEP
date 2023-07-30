package com.lighthouse.beep.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.lighthouse.beep.ui.BeepAppState
import com.lighthouse.beep.ui.feature.login.navigation.loginScreen
import com.lighthouse.beep.ui.feature.main.navigation.mainGraph

@Composable
fun BeepNavHost(
    navController: NavHostController,
    appState: BeepAppState,
    startDestination: TopLevelDestination,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
    ) {
        loginScreen(
            route = TopLevelDestination.LOGIN.route,
            navController = navController,
            navigateToMain = {
                appState.navigateToTopLevelDestination(TopLevelDestination.MAIN)
            },
        )
        mainGraph(
            route = TopLevelDestination.MAIN.route,
            navigateToMap = {},
            navigateToAdd = {},
            navigateToSetting = {},
        ) {
        }
    }
}
