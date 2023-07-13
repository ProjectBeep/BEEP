package com.lighthouse.beep.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.lighthouse.beep.ui.page.intro.navigation.GRAPH_INTRO
import com.lighthouse.beep.ui.page.intro.navigation.introGraph

@Composable
fun BeepNavHost(
    navController: NavHostController,
    startDestination: String = GRAPH_INTRO,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
    ) {
        introGraph()
    }
}
