package com.lighthouse.beep.permission.dialog

import com.lighthouse.beep.permission.BeepPermission
import com.lighthouse.beep.permission.R
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationParam

class NotificationPermissionDialog: PermissionDialog(BeepPermission.notification) {

    companion object {
        const val TAG = "notification"
    }

    init {
        arguments = ConfirmationParam(
            messageResId = R.string.notification_permission_message,
            okTextResId = R.string.permission_allow,
            okDismiss = false,
            cancelTextResId = R.string.permission_disallow,
        ).buildBundle()
    }
}