package com.lighthouse.beep.ui.feature.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lighthouse.beep.ui.feature.main.page.main.MainScreen

internal const val ROUTE_MAIN = "route.main"

fun NavGraphBuilder.mainGraph(
    route: String,
    navigateToMap: () -> Unit,
    navigateToAdd: () -> Unit,
    navigateToSetting: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit = {},
) {
    navigation(
        route = route,
        startDestination = ROUTE_MAIN,
    ) {
        composable(route = ROUTE_MAIN) {
            MainScreen(
                navigateToMap = navigateToMap,
                navigateToAdd = navigateToAdd,
                navigateToSetting = navigateToSetting,
            )
        }
        nestedGraphs()
    }
}
