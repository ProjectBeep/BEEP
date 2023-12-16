package com.lighthouse.beep.permission.dialog

import com.lighthouse.beep.permission.BeepPermission
import com.lighthouse.beep.permission.R
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationParam

class StoragePermissionDialog: PermissionDialog(BeepPermission.storage) {

    companion object {
        const val TAG = "storage"
    }

    init {
        arguments = ConfirmationParam(
            messageResId = R.string.storage_permission_message,
            okTextResId = R.string.permission_allow,
            okDismiss = false,
            cancelTextResId = R.string.permission_disallow,
        ).buildBundle()
    }
}