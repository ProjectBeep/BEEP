package com.lighthouse.beep.permission.dialog

fun interface OnPermissionListener {

    fun onPermissionResult(grant: Boolean)
}