package com.lighthouse.features.setting.model

import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.features.setting.R

internal enum class SettingMenu(val uiText: UIText) {

    USED_GIFTICON(UIText.StringResource(R.string.used_gifticon)),
    IMMINENT_NOTIFICATION(UIText.StringResource(R.string.config_imminent_notification)),
    SECURITY(UIText.StringResource(R.string.config_security)),
    LOCATION(UIText.StringResource(R.string.config_location_permission)),
    SIGN_IN(UIText.StringResource(R.string.user_sign_in)),
    SIGN_OUT(UIText.StringResource(R.string.user_sign_out)),
    WITHDRAWAL(UIText.StringResource(R.string.user_withdrawal)),
    COFFEE(UIText.StringResource(R.string.etc_coffee)),
    TERMS_OF_USE(UIText.StringResource(R.string.etc_terms_of_use)),
    PERSONAL_INFO_POLICY(UIText.StringResource(R.string.etc_personal_info_policy)),
    OPEN_SOURCE_LICENSE(UIText.StringResource(R.string.etc_open_source_license))
}
