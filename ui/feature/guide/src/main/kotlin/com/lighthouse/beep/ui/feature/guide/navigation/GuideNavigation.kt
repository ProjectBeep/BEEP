package com.lighthouse.beep.ui.feature.guide.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.lighthouse.beep.ui.feature.guide.page.permission.GuidePermissionScreen

internal const val ROUTE_GUIDE_PERMISSION = "route.guide.permission"

fun NavController.navigateToGuidePermission(navOptions: NavOptions? = null) {
    navigate(ROUTE_GUIDE_PERMISSION, navOptions)
}

fun NavGraphBuilder.guidePermissionScreen() {
    composable(route = ROUTE_GUIDE_PERMISSION) {
        GuidePermissionScreen()
    }
}
