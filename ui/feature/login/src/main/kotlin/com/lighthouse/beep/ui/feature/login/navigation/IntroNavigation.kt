package com.lighthouse.beep.ui.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import androidx.navigation.navigation
import com.lighthouse.beep.ui.feature.login.page.login.LoginRoute
import com.lighthouse.beep.ui.feature.login.page.permission.GuidePermissionRoute

internal const val ROUTE_LOGIN = "route.login"
internal const val ROUTE_PERMISSION = "route.permission"

internal fun NavController.navigateToPermission() {
    val navOptions = navOptions {
        popUpTo(0)
        launchSingleTop = true
    }

    navigate(ROUTE_PERMISSION, navOptions)
}

fun NavGraphBuilder.loginScreen(
    route: String,
    navController: NavController,
    navigateToMain: () -> Unit = {},
) {
    navigation(
        route = route,
        startDestination = ROUTE_LOGIN,
    ) {
        composable(route = ROUTE_LOGIN) {
            LoginRoute(
                onNavigatePermission = {
                    navController.navigateToPermission()
                },
            )
        }
        composable(route = ROUTE_PERMISSION) {
            GuidePermissionRoute(
                navigateToMain = navigateToMain,
            )
        }
    }
}
