package com.lighthouse.beep.permission.dialog

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import com.lighthouse.beep.core.ui.exts.checkSelfPermissions
import com.lighthouse.beep.ui.dialog.confirmation.ConfirmationDialog

open class PermissionDialog(
    private val permissions: Array<String>,
) : ConfirmationDialog() {

    private val launchPermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onPermissionListener?.onPermissionResult(checkSelfPermissions(permissions))
            dismiss()
        }

    init {
        setOnOkClickListener {
            val uri = Uri.parse("package:${context?.packageName}")
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
            launchPermission.launch(intent)
        }
    }

    private var onPermissionListener: OnPermissionListener? = null

    fun setOnPermissionListener(listener: OnPermissionListener?) {
        onPermissionListener = listener
    }
}