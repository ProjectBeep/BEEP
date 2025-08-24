package com.lighthouse.beep.core.common.utils.log

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.util.Log

object MSLog {

    private val logger = MSLogger(
        usePrefix = true,
    )
    private val toolLogger = MSLogger(
        usePrefix = false,
    )

    private val fileManager = MSFileManager(toolLogger)
    private val timeChecker = MSTimeChecker(toolLogger)

    fun d(message: String) {
        logger.d(message)
    }

    fun d(tag: String, message: String) {
        logger.d(tag, message)
        // 직접 Log.d도 호출하여 확실한 출력 보장
        Log.d("BEEP_$tag", message)
    }

    fun i(message: String) {
        logger.i(message)
    }

    fun i(tag: String, message: String) {
        logger.i(tag, message)
    }

    fun v(message: String) {
        logger.v(message)
    }

    fun v(tag: String, message: String) {
        logger.v(tag, message)
    }

    fun w(message: String) {
        logger.w(message)
    }

    fun w(tag: String, message: String) {
        logger.w(tag, message)
    }

    fun w(tag: String, message: String, throwable: Throwable) {
        logger.w(tag, message, throwable)
    }

    fun e(message: String) {
        logger.e(message)
    }

    fun e(message: String, throwable: Throwable) {
        logger.e(message, throwable)
    }

    fun e(tag: String, message: String) {
        logger.e(tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable) {
        logger.e(tag, message, throwable)
    }

    fun stackTrace(message: String) {
        logger.stackTrace(message)
    }

    fun stackTrace(tag: String, message: String) {
        logger.stackTrace(tag, message)
    }

    fun tick(tag: String, message: String = "") {
        timeChecker.tick(tag, message)
    }

    fun tock(tag: String, message: String = "") {
        timeChecker.tock(tag, message)
    }

    fun saveBitmap(
        bitmap: Bitmap?,
        fileName: String = "${System.currentTimeMillis()}",
        format: CompressFormat = CompressFormat.JPEG
    ) {
        fileManager.saveBitmap(bitmap, fileName, format)
    }
}