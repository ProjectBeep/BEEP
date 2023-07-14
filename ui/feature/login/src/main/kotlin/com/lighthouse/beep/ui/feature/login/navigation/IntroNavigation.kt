package com.lighthouse.beep.ui.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.lighthouse.beep.ui.feature.login.page.login.IntroScreen
import com.lighthouse.beep.ui.feature.login.page.permission.PermissionScreen

const val GRAPH_LOGIN = "graph.login"
internal const val ROUTE_LOGIN = "route.login"
internal const val ROUTE_PERMISSION = "route.permission"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(GRAPH_LOGIN, navOptions)
}

internal fun NavController.navigateToPermission() {
    navigate(
        ROUTE_PERMISSION,
        navOptions {
            popUpTo(ROUTE_LOGIN) {
                inclusive = true
            }
        },
    )
}

fun NavGraphBuilder.loginGraph(
    navController: NavController,
    navigateToMain: () -> Unit,
) {
    navigation(
        route = GRAPH_LOGIN,
        startDestination = ROUTE_LOGIN,
    ) {
        composable(route = ROUTE_LOGIN) {
            IntroScreen(navigateToPermission = {
                navController.navigateToPermission()
            })
        }
        composable(route = ROUTE_PERMISSION) {
            PermissionScreen(navigateToMain = navigateToMain)
        }
    }
}
