package com.lighthouse.beep.ui.feature.main.page.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lighthouse.beep.theme.BeepColor
import com.lighthouse.beep.theme.BeepTextStyle
import com.lighthouse.beep.theme.BeepTheme
import com.lighthouse.beep.ui.feature.main.R

@Composable
fun MainScreen(
    navigateToMap: () -> Unit = {},
    navigateToAdd: () -> Unit = {},
    navigateToSetting: () -> Unit = {},
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(BeepColor.Grey95)
            .displayCutoutPadding(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            Text(
                text = stringResource(id = R.string.main_title),
                style = BeepTextStyle.TitleLarge,
                color = BeepColor.Pink,
            )

            Row {
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    BeepTheme {
        MainScreen()
    }
}
