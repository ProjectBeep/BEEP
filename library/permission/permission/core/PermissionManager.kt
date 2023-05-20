package com.lighthouse.core.android.utils.permission.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

abstract class PermissionManager(
    protected val context: Context
) {
    protected abstract val permissions: Array<String>

    protected open val additionalPermissions = emptyArray<String>()

    fun getPermissions(type: PermissionType = PermissionType.Basic): Array<String> {
        return when (type) {
            PermissionType.All -> permissions + additionalPermissions
            PermissionType.Basic -> permissions
            PermissionType.Additional -> additionalPermissions
        }.copyOf()
    }

    fun isGrant(type: PermissionType = PermissionType.Basic): Boolean {
        val list = when (type) {
            PermissionType.All -> permissions + additionalPermissions
            PermissionType.Basic -> permissions
            PermissionType.Additional -> additionalPermissions
        }
        return list.all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

    fun getManyTimesRejectedPermission(type: PermissionType = PermissionType.Basic): String? {
        val activity = context as? Activity ?: return null
        val permissions = getPermissions(type)
        for (permission in permissions) {
            if (activity.shouldShowRequestPermissionRationale(permission)) {
                return permission
            }
        }
        return null
    }

    open fun settingIntent(): Intent {
        return Intent()
    }
}
