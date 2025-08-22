package com.lighthouse.beep.permission.dialog

import com.lighthouse.beep.permission.BeepPermission
import com.lighthouse.beep.permission.R
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationParam

class LocationPermissionDialog: PermissionDialog(BeepPermission.location) {

    companion object {
        const val TAG = "location"
    }

    init {
        arguments = ConfirmationParam(
            messageResId = R.string.location_permission_message,
            okTextResId = R.string.permission_allow,
            okDismiss = false,
            cancelTextResId = R.string.permission_disallow,
        ).buildBundle()
    }
}