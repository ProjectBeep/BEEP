package com.lighthouse.core.android.utils.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.lighthouse.core.android.utils.permission.core.PermissionManager

class LocationPermissionManager(context: Context) : PermissionManager(context) {

    override val additionalPermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    } else {
        emptyArray()
    }

    override val permissions =
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    override fun settingIntent(): Intent {
        return Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null)
        )
    }
}
