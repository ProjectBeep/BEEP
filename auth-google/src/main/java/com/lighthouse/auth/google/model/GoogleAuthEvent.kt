package com.lighthouse.auth.google.model

import com.lighthouse.core.android.utils.resource.UIText

sealed class GoogleAuthEvent {

    data class SnackBar(val text: UIText) : GoogleAuthEvent()
}
