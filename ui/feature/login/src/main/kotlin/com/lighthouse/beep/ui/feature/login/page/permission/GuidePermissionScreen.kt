package com.lighthouse.beep.ui.feature.login.page.permission

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.lighthouse.beep.core.ui.exts.shadow
import com.lighthouse.beep.library.permission.BeepPermission
import com.lighthouse.beep.theme.BeepColor
import com.lighthouse.beep.theme.BeepShape
import com.lighthouse.beep.theme.BeepTextStyle
import com.lighthouse.beep.theme.BeepTheme
import com.lighthouse.beep.ui.feature.login.R

@Composable
internal fun GuidePermissionRoute(
    navigateToMain: () -> Unit = {},
    viewModel: GuidePermissionViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.shownGuidePermission()
    }

    GuidePermissionScreen(
        items = viewModel.items,
        navigateToMain = navigateToMain,
    )
}

@Composable
internal fun GuidePermissionScreen(
    items: List<GuidePermissionData> = listOf(),
    navigateToMain: () -> Unit = {},

) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        navigateToMain()
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = stringResource(id = R.string.guide_permission_title),
            color = BeepColor.Grey30,
            style = BeepTextStyle.TitleLarge,
        )
        Text(
            text = stringResource(id = R.string.guide_permission_description),
            color = BeepColor.Grey30,
            style = BeepTextStyle.TitleSmall,
        )
        Spacer(modifier = Modifier.height(48.dp))
        GuidePermissionList(permissionList = items)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.guide_permission_caution),
            color = BeepColor.Grey70,
            style = BeepTextStyle.BodySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(28.dp))
        GuidePermissionAgreeButton(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            val permissions = BeepPermission.all.filter { permission ->
                ContextCompat.checkSelfPermission(
                    context,
                    permission,
                ) != PackageManager.PERMISSION_GRANTED
            }.toTypedArray()

            if (permissions.isEmpty()) {
                navigateToMain()
            } else {
                permissionLauncher.launch(permissions)
            }
        }
        Spacer(modifier = Modifier.size(44.dp))
    }
}

@Composable
internal fun GuidePermissionList(
    permissionList: List<GuidePermissionData>,
) {
    Surface(
        modifier = Modifier.width(246.dp),
        color = BeepColor.Grey95,
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 36.dp),
        ) {
            Text(
                text = stringResource(id = R.string.guide_permission_list),
                color = BeepColor.Grey30,
                style = BeepTextStyle.TitleMedium,
            )
            Spacer(modifier = Modifier.size(20.dp))
            val iterator = permissionList.iterator()
            while (iterator.hasNext()) {
                val permissionData = iterator.next()
                GuidePermissionItem(
                    painter = painterResource(id = permissionData.iconRes),
                    titleRes = permissionData.titleRes,
                    descriptionRes = permissionData.descriptionRes,
                )
                if (iterator.hasNext()) {
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
internal fun GuidePermissionItem(
    painter: Painter,
    @StringRes titleRes: Int,
    @StringRes descriptionRes: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(40.dp)
                .shadow(
                    color = Color.Black.copy(alpha = 0.05f),
                    blur = 10.dp,
                ),
            painter = painter,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(13.dp))
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = titleRes),
                color = BeepColor.Grey30,
                style = BeepTextStyle.TitleSmall,
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = stringResource(id = descriptionRes),
                color = BeepColor.Grey30,
                style = BeepTextStyle.BodySmall,
            )
        }
    }
}

@Composable
internal fun GuidePermissionAgreeButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = BeepColor.Pink,
        shape = BeepShape.ButtonShape,
    ) {
        Box(
            modifier = Modifier
                .clickable { onClick() }
                .padding(10.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.guide_permission_agree),
                color = BeepColor.White,
                style = BeepTextStyle.TitleMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun GuidePermissionScreenPreview() {
    val items = listOf(
        GuidePermissionData(
            iconRes = R.drawable.icon_permission_notification,
            titleRes = R.string.guide_permission_notification,
            descriptionRes = R.string.guide_permission_notification_description,
        ),
        GuidePermissionData(
            iconRes = R.drawable.icon_permission_picture,
            titleRes = R.string.guide_permission_picture,
            descriptionRes = R.string.guide_permission_picture_description,
        ),
        GuidePermissionData(
            iconRes = R.drawable.icon_permission_location,
            titleRes = R.string.guide_permission_location,
            descriptionRes = R.string.guide_permission_location_description,
        ),
    )

    BeepTheme {
        GuidePermissionScreen(
            items = items,
        )
    }
}
