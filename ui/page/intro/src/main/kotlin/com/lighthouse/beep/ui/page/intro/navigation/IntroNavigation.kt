package com.lighthouse.beep.ui.page.intro.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

fun NavController.navigateToIntro() {
    navigate("intro") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.introScreen() {
    composable(
        route = "intro",
    ) {
    }
}
