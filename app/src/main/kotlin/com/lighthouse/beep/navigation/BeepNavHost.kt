package com.lighthouse.beep.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lighthouse.beep.ui.BeepAppState
import com.lighthouse.beep.ui.feature.guide.navigation.guidePermissionScreen
import com.lighthouse.beep.ui.feature.login.navigation.loginScreen
import com.lighthouse.beep.ui.feature.main.navigation.mainGraph

@Composable
fun BeepNavHost(
    navController: NavHostController,
    appState: BeepAppState,
) {
    NavHost(
        navController = navController,
        startDestination = "none",
    ) {
        composable("none") { }
        loginScreen()
        mainGraph(
            navigateToMap = {},
            navigateToAdd = {},
            navigateToSetting = {},
        ) {
        }
        guidePermissionScreen()
    }
}
