package com.lighthouse.beep.ui.page.intro

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lighthouse.beep.theme.BodyMedium
import com.lighthouse.beep.theme.BodySmall
import com.lighthouse.beep.theme.ButtonShape
import com.lighthouse.beep.theme.Grey30
import com.lighthouse.beep.theme.Grey50
import com.lighthouse.beep.theme.Grey70
import com.lighthouse.beep.theme.TitleLarge
import com.lighthouse.beep.theme.TitleMedium
import com.lighthouse.beep.theme.TitleSmall
import com.lighthouse.beep.ui.page.intro.icon.BeepIcon
import com.lighthouse.beep.ui.page.intro.icon.Right

@Composable
internal fun IntroRoute() {
    IntroScreen()
}

@Composable
fun IntroScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = TitleLarge,
            color = Grey30,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            text = stringResource(id = R.string.app_description),
            style = TitleMedium,
            color = Grey50,
        )
        Spacer(modifier = Modifier.size(36.dp))
        Image(
            modifier = Modifier.size(144.dp),
            painter = painterResource(id = R.drawable.img_google_logo),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(12.dp))
        Surface {
            Spacer(modifier = Modifier.size(8.dp))
        }
        Spacer(modifier = Modifier.size(80.dp))

        Text(
            text = stringResource(id = R.string.login_method),
            style = BodyMedium,
            color = Grey50,
        )
        Spacer(modifier = Modifier.size(12.dp))
        LoginButton(
            textRes = R.string.naver_login,
            textColorRes = R.color.naver_label,
            backgroundColorRes = R.color.naver_container,
            iconRes = R.drawable.img_google_logo,
            onClick = {
            },
        )
        Spacer(modifier = Modifier.size(12.dp))
        LoginButton(
            textRes = R.string.kakao_login,
            textColorRes = R.color.kakao_label,
            backgroundColorRes = R.color.kakao_container,
            iconRes = R.drawable.img_google_logo,
            onClick = {
            },
        )
        Spacer(modifier = Modifier.size(12.dp))
        LoginButton(
            textRes = R.string.google_login,
            textColorRes = R.color.google_label,
            backgroundColorRes = R.color.google_container,
            iconRes = R.drawable.img_google_logo,
            onClick = {
            },
        )
        Spacer(modifier = Modifier.size(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.login_description),
                style = BodySmall,
                color = Grey70,
            )
            Spacer(modifier = Modifier.size(12.dp))
            GuestButton(
                onClick = {
                },
            )
        }
    }
}

@Composable
internal fun LoginButton(
    @StringRes textRes: Int,
    @ColorRes textColorRes: Int,
    @ColorRes backgroundColorRes: Int,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .padding(horizontal = 16.dp),
        color = colorResource(id = backgroundColorRes),
        shape = ButtonShape,
    ) {
        Box(
            modifier = Modifier.clickable {
                onClick()
            },
        ) {
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterStart),
                painter = painterResource(id = iconRes),
                contentDescription = null,
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = textRes),
                color = colorResource(id = textColorRes),
                style = TitleSmall,
            )
        }
    }
}

@Composable
internal fun GuestButton(
    onClick: () -> Unit = {},
) {
    Surface(
        shape = ButtonShape,
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(8.dp),
        ) {
            Text(
                text = stringResource(id = R.string.guest_login),
                style = BodySmall,
                color = Grey30,
            )
            Image(
                modifier = Modifier.size(18.dp),
                imageVector = BeepIcon.Right,
                contentDescription = null,
            )
        }
    }
}
