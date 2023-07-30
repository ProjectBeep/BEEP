package com.lighthouse.beep.ui.feature.login.page.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lighthouse.auth.google.GoogleTokenResult
import com.lighthouse.auth.google.local.LocalGoogleClient
import com.lighthouse.beep.auth.kakao.KakaoTokenResult
import com.lighthouse.beep.auth.kakao.local.LocalKakaoClient
import com.lighthouse.beep.auth.naver.NaverTokenResult
import com.lighthouse.beep.auth.naver.local.LocalNaverClient
import com.lighthouse.beep.core.ui.compose.rememberLifecycleEvent
import com.lighthouse.beep.model.user.AuthProvider
import com.lighthouse.beep.theme.BeepColor
import com.lighthouse.beep.theme.BeepShape
import com.lighthouse.beep.theme.BeepTextStyle
import com.lighthouse.beep.theme.BeepTheme
import com.lighthouse.beep.ui.designsystem.dotindicator.DotIndicator
import com.lighthouse.beep.ui.designsystem.dotindicator.DotShape
import com.lighthouse.beep.ui.designsystem.dotindicator.type.WormType
import com.lighthouse.beep.ui.feature.login.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
internal fun LoginRoute(
    onNavigatePermission: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val isLoading by viewModel.loadingState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loginEvent.collect {
            if (it.isSuccess) {
                onNavigatePermission()
            }
        }
    }

    val naverClient = LocalNaverClient.current
    val requestNaverLogin =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (val tokenResult = naverClient.getAccessToken(result)) {
                is NaverTokenResult.Success -> {
                    viewModel.requestLogin(AuthProvider.NAVER, tokenResult.accessToken)
                }

                is NaverTokenResult.Failed -> {
                }

                is NaverTokenResult.Canceled -> {
                }
            }
        }

    val kakaoClient = LocalKakaoClient.current

    val googleClient = LocalGoogleClient.current
    val requestGoogleLogin =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            coroutineScope.launch {
                when (val tokenResult = googleClient.getAccessToken(result)) {
                    is GoogleTokenResult.Success -> {
                        viewModel.requestLogin(AuthProvider.GOOGLE, tokenResult.idToken)
                    }

                    is GoogleTokenResult.Failed -> {
                    }

                    is GoogleTokenResult.Canceled -> {
                    }
                }
            }
        }

    LoginScreen(
        isLoading = isLoading,
        items = viewModel.items,
        onNaverLoginClick = {
            naverClient.requestAccessToken(requestNaverLogin) { token ->
                viewModel.requestLogin(AuthProvider.NAVER, token)
            }
        },
        onKakaoLoginClick = {
            coroutineScope.launch {
                when (val tokenResult = kakaoClient.getAccessToken(context)) {
                    is KakaoTokenResult.Success -> {
                        viewModel.requestLogin(AuthProvider.KAKAO, tokenResult.accessToken)
                    }

                    is KakaoTokenResult.Failed -> {
                    }

                    is KakaoTokenResult.Canceled -> {
                    }
                }
            }
        },
        onGoogleLoginClick = {
            val intent = googleClient.signInIntent
            requestGoogleLogin.launch(intent)
        },
        onGuestLoginClick = {
            viewModel.requestLogin(AuthProvider.GUEST, "")
        },
    )
}

@Composable
internal fun LoginScreen(
    isLoading: Boolean = false,
    items: List<LoginData> = listOf(),
    onNaverLoginClick: () -> Unit = {},
    onKakaoLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    onGuestLoginClick: () -> Unit = {},
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            LoginPager(items = items)
            Spacer(modifier = Modifier.weight(1f))
            LoginButtonsScreen(
                onNaverLoginClick = onNaverLoginClick,
                onKakaoLoginClick = onKakaoLoginClick,
                onGoogleLoginClick = onGoogleLoginClick,
                onGuestLoginClick = onGuestLoginClick,
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
        if (isLoading) {
            LoadingScreen()
        }
    }
}

@Composable
internal fun LoadingScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black.copy(alpha = 0.3f),
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = BeepColor.Pink)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LoginPager(
    items: List<LoginData> = listOf(),
) {
    val lifecycleEvent = rememberLifecycleEvent()
    val pagerState = rememberPagerState()

    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            var autoScrollJob: Job? = startAutoScroll(this, pagerState, items.size)
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
                    startAutoScroll(this, pagerState, items.size)
                }
            }
        }
    }

    Surface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            HorizontalPager(
                pageCount = items.size,
                state = pagerState,
            ) { index ->
                val item = items[index]
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = item.titleRes),
                        style = BeepTextStyle.TitleLarge,
                        color = BeepColor.Grey30,
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = stringResource(id = item.descriptionRes),
                        style = BeepTextStyle.TitleMedium,
                        color = BeepColor.Grey50,
                    )
                    Spacer(modifier = Modifier.size(20.dp))
                    IntroImage(lottieRes = item.lottieRes)
                }
            }
            Spacer(modifier = Modifier.size(20.dp))
            DotIndicator(
                dotCount = items.size,
                pagerState = pagerState,
                dotType = WormType(
                    dotShape = DotShape(color = BeepColor.Grey95),
                    wormDotShape = DotShape(color = BeepColor.Pink50),
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
internal fun LoginButtonsScreen(
    onNaverLoginClick: () -> Unit = {},
    onKakaoLoginClick: () -> Unit = {},
    onGoogleLoginClick: () -> Unit = {},
    onGuestLoginClick: () -> Unit = {},
) {
    Text(
        text = stringResource(id = R.string.login_method),
        style = BeepTextStyle.BodyMedium,
        color = BeepColor.Grey50,
    )
    Spacer(modifier = Modifier.size(12.dp))
    LoginButton(
        textRes = R.string.naver_login,
        textColorRes = R.color.naver_label,
        backgroundColorRes = R.color.naver_container,
        iconRes = R.drawable.icon_naver,
        iconTintRes = R.color.naver_label,
        onClick = onNaverLoginClick,
    )
    Spacer(modifier = Modifier.size(12.dp))
    LoginButton(
        textRes = R.string.kakao_login,
        textColorRes = R.color.kakao_label,
        backgroundColorRes = R.color.kakao_container,
        iconRes = R.drawable.icon_kakao,
        onClick = onKakaoLoginClick,
    )
    Spacer(modifier = Modifier.size(12.dp))
    LoginButton(
        textRes = R.string.google_login,
        textColorRes = R.color.google_label,
        backgroundColorRes = R.color.google_container,
        iconRes = R.drawable.icon_google,
        iconBackgroundColorRes = R.color.google_symbol_background,
        onClick = onGoogleLoginClick,
    )
    Spacer(modifier = Modifier.size(10.dp))
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.login_description),
            style = BeepTextStyle.BodySmall,
            color = BeepColor.Grey70,
        )
        Spacer(modifier = Modifier.size(8.dp))
        GuestButton(onGuestLoginClick)
    }
}

@Composable
internal fun LoginButton(
    @StringRes textRes: Int,
    @ColorRes textColorRes: Int,
    @ColorRes backgroundColorRes: Int,
    @DrawableRes iconRes: Int,
    @ColorRes iconTintRes: Int? = null,
    @ColorRes iconBackgroundColorRes: Int = backgroundColorRes,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(horizontal = 16.dp),
        color = colorResource(id = backgroundColorRes),
        shape = BeepShape.ButtonShape,
    ) {
        Box(
            modifier = Modifier
                .clickable { onClick() },
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Spacer(modifier = Modifier.size(6.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(colorResource(id = iconBackgroundColorRes), CircleShape),
                ) {
                    Image(
                        modifier = Modifier
                            .size(20.dp)
                            .align(Alignment.Center),
                        painter = painterResource(id = iconRes),
                        colorFilter = iconTintRes?.let { ColorFilter.tint(colorResource(id = it)) },
                        contentDescription = null,
                    )
                }
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = textRes),
                color = colorResource(id = textColorRes),
                style = BeepTextStyle.TitleSmall,
            )
        }
    }
}

@Composable
internal fun GuestButton(
    onClick: () -> Unit = {},
) {
    Surface(
        shape = RoundedCornerShape(5.dp),
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(4.dp),
        ) {
            Text(
                text = stringResource(id = R.string.guest_login),
                style = BeepTextStyle.BodySmall,
                color = BeepColor.Grey30,
            )
            Image(
                painter = painterResource(id = R.drawable.icon_right),
                colorFilter = ColorFilter.tint(BeepColor.Grey70),
                modifier = Modifier.size(16.dp),
                contentDescription = null,
            )
        }
    }
}

@Preview
@Composable
internal fun PreviewGuestButton() {
    val items = listOf(
        LoginData(
            titleRes = R.string.app_name,
            descriptionRes = R.string.app_description,
            lottieRes = R.raw.lottie_anim1,
        ),
        LoginData(
            titleRes = R.string.recognize_name,
            descriptionRes = R.string.recognize_description,
            lottieRes = R.raw.lottie_anim2,
        ),
        LoginData(
            titleRes = R.string.map_name,
            descriptionRes = R.string.map_description,
            lottieRes = R.raw.lottie_anim3,
        ),
    )

    BeepTheme {
        LoginScreen(
            isLoading = true,
            items = items,
        )
    }
}
