package com.lighthouse.beep.ui

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview

@Composable
internal fun BeepSnackBarHost(hostState: SnackbarHostState) {
    SnackbarHost(
        hostState = hostState,
    ) { snackbarData ->
    }
}

@Preview
@Composable
internal fun BeepSnackBarHostPreview() {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

//    Scaffold(
//        modifier = Modifier.fillMaxSize()
//    ) {
//
//    }
}
