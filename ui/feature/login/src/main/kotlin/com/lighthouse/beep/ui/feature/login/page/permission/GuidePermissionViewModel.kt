package com.lighthouse.beep.ui.feature.login.page.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.domain.usecase.setting.guide.SetShownGuidePermission
import com.lighthouse.beep.ui.feature.login.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GuidePermissionViewModel @Inject constructor(
    private val setShownGuidePermission: SetShownGuidePermission,
) : ViewModel() {

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

    fun shownGuidePermission() {
        viewModelScope.launch {
            setShownGuidePermission(true)
        }
    }
}
