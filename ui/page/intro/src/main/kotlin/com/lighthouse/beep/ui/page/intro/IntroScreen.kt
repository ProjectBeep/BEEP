package com.lighthouse.beep.ui.page.intro

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lighthouse.beep.theme.BeepTheme
import com.lighthouse.beep.theme.BodyMedium
import com.lighthouse.beep.theme.BodySmall
import com.lighthouse.beep.theme.ButtonShape
import com.lighthouse.beep.theme.Grey30
import com.lighthouse.beep.theme.Grey50
import com.lighthouse.beep.theme.Grey70
import com.lighthouse.beep.theme.Grey95
import com.lighthouse.beep.theme.TitleLarge
import com.lighthouse.beep.theme.TitleMedium
import com.lighthouse.beep.theme.TitleSmall
import com.lighthouse.beep.ui.designsystem.dotindicator.DotIndicator
import com.lighthouse.beep.ui.designsystem.dotindicator.DotShape
import com.lighthouse.beep.ui.designsystem.dotindicator.type.WormType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
internal fun IntroRoute() {
    IntroScreen()
}

@Composable
fun IntroScreen(
    viewModel: IntroViewModel = hiltViewModel(),
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.surface),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IntroPager(
            list = viewModel.items,
        )
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
        Spacer(modifier = Modifier.size(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(id = R.string.login_description),
                style = BodySmall,
                color = Grey70,
            )
            Spacer(modifier = Modifier.size(8.dp))
            GuestButton(
                onClick = {
                },
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun IntroPager(
    list: List<IntroData> = listOf(),
) {
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    LaunchedEffect(Unit) {
        var autoScrollJob: Job? = startAutoScroll(coroutineScope, pagerState, list.size)
        pagerState.interactionSource.interactions.collect { interaction ->
            val interactive = when (interaction) {
                is PressInteraction.Press -> true
                is DragInteraction.Start -> true
                else -> false
            }
            autoScrollJob = if (interactive) {
                autoScrollJob?.cancel()
                null
            } else {
                startAutoScroll(coroutineScope, pagerState, list.size)
            }
        }
    }

    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalPager(
                pageCount = list.size,
                state = pagerState,
            ) { index ->
                val item = list[index]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = item.titleRes),
                        style = TitleLarge,
                        color = Grey30,
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(
                        text = stringResource(id = item.descriptionRes),
                        style = TitleMedium,
                        color = Grey50,
                    )
                    Spacer(modifier = Modifier.size(36.dp))
                    IntroImage(lottieRes = item.lottieRes)
                }
            }
            Spacer(modifier = Modifier.size(12.dp))
            DotIndicator(
                dotCount = list.size,
                pagerState = pagerState,
                dotType = WormType(
                    dotShape = DotShape(color = Grey95),
                    wormDotShape = DotShape(color = Color(0xFFFF94B4)),
                ),
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
internal fun startAutoScroll(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    pagerSize: Int,
): Job {
    return coroutineScope.launch {
        while (isActive) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % pagerSize
            pagerState.animateScrollToPage(nextPage)
        }
    }
}

@Composable
internal fun IntroImage(
    @RawRes lottieRes: Int,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(lottieRes),
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
    LottieAnimation(
        modifier = Modifier.size(150.dp),
        composition = composition,
        progress = { progress },
    )
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
                .padding(vertical = 6.dp, horizontal = 12.dp),
        ) {
            Text(
                text = stringResource(id = R.string.guest_login),
                style = BodySmall,
                color = Grey30,
            )
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.icon_right)
                    .build(),
                colorFilter = ColorFilter.tint(Grey70),
                modifier = Modifier.size(18.dp),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewGuestButton() {
    BeepTheme {
        IntroScreen()
    }
}
