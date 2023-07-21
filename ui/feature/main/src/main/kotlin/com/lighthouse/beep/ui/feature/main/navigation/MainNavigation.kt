package com.lighthouse.beep.ui.feature.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.lighthouse.beep.ui.feature.main.page.main.MainScreen

const val GRAPH_MAIN = "graph.main"
internal const val ROUTE_MAIN = "route.main"

fun NavController.navigateToMain(navOptions: NavOptions? = null) {
    navigate(GRAPH_MAIN, navOptions)
}

fun NavGraphBuilder.mainGraph(
    navigateToMap: () -> Unit,
    navigateToAdd: () -> Unit,
    navigateToSetting: () -> Unit,
    nestedGraphs: NavGraphBuilder.() -> Unit = {},
) {
    navigation(
        route = GRAPH_MAIN,
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
