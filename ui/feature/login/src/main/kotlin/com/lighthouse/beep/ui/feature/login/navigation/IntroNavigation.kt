package com.lighthouse.beep.ui.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.lighthouse.beep.ui.feature.login.page.login.IntroScreen
import com.lighthouse.beep.ui.feature.login.page.permission.GuidePermissionScreen

internal const val GRAPH_LOGIN = "graph.login"

internal const val ROUTE_LOGIN = "route.login"
internal const val ROUTE_PERMISSION = "route.permission"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(ROUTE_LOGIN, navOptions)
}

internal fun NavController.navigateToPermission() {
    val navOptions = navOptions {
        popUpTo(0)
        launchSingleTop = true
    }

    navigate(ROUTE_PERMISSION, navOptions)
}

fun NavGraphBuilder.loginScreen(
    navController: NavController,
    onNavigateMain: () -> Unit = {},
) {
    navigation(
        route = GRAPH_LOGIN,
        startDestination = ROUTE_LOGIN,
    ) {
        composable(route = ROUTE_LOGIN) {
            IntroScreen(
                onNavigatePermission = {
                    navController.navigateToPermission()
                },
            )
        }
        composable(route = ROUTE_PERMISSION) {
            GuidePermissionScreen(
                onNavigateMain = onNavigateMain,
            )
        }
    }
}
