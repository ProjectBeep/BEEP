package com.lighthouse.beep.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.lighthouse.beep.ui.feature.login.navigation.GRAPH_LOGIN
import com.lighthouse.beep.ui.feature.login.navigation.loginGraph
import com.lighthouse.beep.ui.feature.main.navigation.mainGraph
import com.lighthouse.beep.ui.feature.main.navigation.navigateToMain

@Composable
fun BeepNavHost(
    navController: NavHostController,
    startDestination: String = GRAPH_LOGIN,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        loginGraph(navController, navigateToMain = {
            navController.navigateToMain()
        })
        mainGraph(
            navigateToMap = {},
            navigateToAdd = {},
            navigateToSetting = {},
        ) {
        }
    }
}
