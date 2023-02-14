package com.lighthouse.domain.controller

interface NotificationWorkerController {

    fun enqueue()

    fun cancel()
}
