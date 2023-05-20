package com.lighthouse.features.common.model

import com.lighthouse.core.android.utils.resource.UIText

sealed class MessageEvent {

    data class SnackBar(val uiText: UIText) : MessageEvent()

    data class Toast(val uiText: UIText) : MessageEvent()
}
