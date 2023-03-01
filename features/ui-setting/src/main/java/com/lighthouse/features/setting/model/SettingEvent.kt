package com.lighthouse.features.setting.model

import com.lighthouse.core.android.utils.resource.UIText

internal sealed class SettingEvent {

    data class SnackBar(val text: UIText) : SettingEvent()
}
