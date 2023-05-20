package com.lighthouse.beep.domain.controller

interface NotificationWorkerController {

    fun enqueue()

    fun cancel()
}
