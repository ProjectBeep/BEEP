package com.lighthouse.beep.ui.feature.guide.page.permission

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.lighthouse.beep.theme.BeepTheme
import com.lighthouse.beep.theme.BodySmall
import com.lighthouse.beep.theme.ButtonShape
import com.lighthouse.beep.theme.Grey30
import com.lighthouse.beep.theme.Grey70
import com.lighthouse.beep.theme.Grey95
import com.lighthouse.beep.theme.Pink
import com.lighthouse.beep.theme.TitleLarge
import com.lighthouse.beep.theme.TitleMedium
import com.lighthouse.beep.theme.TitleSmall
import com.lighthouse.beep.theme.White
import com.lighthouse.beep.ui.feature.guide.R

@Composable
internal fun GuidePermissionScreen(
    viewModel: GuidePermissionViewModel = hiltViewModel(),
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            text = stringResource(id = R.string.guide_permission_title),
            color = Grey30,
            style = TitleLarge,
        )
        Text(
            text = stringResource(id = R.string.guide_permission_description),
            color = Grey30,
            style = TitleSmall,
        )
        Spacer(modifier = Modifier.height(48.dp))
        GuidePermissionList(permissionList = viewModel.items)
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(id = R.string.guide_permission_caution),
            color = Grey70,
            style = BodySmall,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(28.dp))
        GuidePermissionAgreeButton(
            modifier = Modifier.padding(horizontal = 20.dp),
        ) {
            viewModel.shownGuidePermission()
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
        color = Grey95,
        shape = RoundedCornerShape(10.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp, bottom = 36.dp),
        ) {
            Text(
                text = stringResource(id = R.string.guide_permission_list),
                color = Grey30,
                style = TitleMedium,
            )
            Spacer(modifier = Modifier.size(20.dp))
            val iterator = permissionList.iterator()
            while (iterator.hasNext()) {
                GuidePermissionItem(iterator.next())
                if (iterator.hasNext()) {
                    Spacer(modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
internal fun GuidePermissionItem(
    permissionData: GuidePermissionData,
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            modifier = Modifier.size(40.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(permissionData.iconRes)
                .build(),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.size(13.dp))
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = permissionData.titleRes),
                color = Grey30,
                style = TitleSmall,
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
                text = stringResource(id = permissionData.descriptionRes),
                color = Grey30,
                style = BodySmall,
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
        color = Pink,
        shape = ButtonShape,
    ) {
        Box(
            modifier = Modifier.clickable { onClick() }
                .padding(10.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.guide_permission_agree),
                color = White,
                style = TitleMedium,
            )
        }
    }
}

@Preview
@Composable
fun PermissionItemPreview() {
    BeepTheme {
        GuidePermissionAgreeButton()
    }
}
