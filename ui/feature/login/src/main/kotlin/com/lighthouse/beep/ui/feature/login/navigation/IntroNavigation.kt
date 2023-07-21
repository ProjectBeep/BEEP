package com.lighthouse.beep.ui.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lighthouse.beep.ui.feature.login.page.login.IntroScreen

internal const val ROUTE_LOGIN = "route.login"

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
    navigate(ROUTE_LOGIN, navOptions)
}

fun NavGraphBuilder.loginScreen() {
    composable(route = ROUTE_LOGIN) {
        IntroScreen()
    }
}
