package com.lighthouse.beep.core.common.utils.log

import android.util.Log

class MSLogger(
    private val usePrefix: Boolean,
) {

    companion object {
        private const val DEFAULT_TAG = "kim_ms"
        private const val REAL_METHOD_POS = 3
    }

    private val exceptFiles = listOf("SJLog", "MSLog", "BLog", "LogHelperImpl")

    private fun isExceptFile(fileName: String): Boolean {
        return exceptFiles.find { fileName.startsWith(it) } != null
    }

    fun d(message: String) {
        d(DEFAULT_TAG, message)
    }

    fun d(tag: String, message: String) {
        Log.d(tag, makeMessage(message))
    }

    fun i(message: String) {
        i(DEFAULT_TAG, makeMessage(message))
    }

    fun i(tag: String, message: String) {
        Log.i(tag, makeMessage(message))
    }

    fun v(message: String) {
        v(DEFAULT_TAG, message)
    }

    fun v(tag: String, message: String) {
        Log.v(tag, makeMessage(message))
    }

    fun w(message: String) {
        w(DEFAULT_TAG, message)
    }

    fun w(tag: String, message: String) {
        Log.w(tag, makeMessage(message))
    }

    fun w(tag: String, message: String, throwable: Throwable) {
        Log.w(tag, makeMessage(message), throwable)
    }

    fun e(message: String) {
        e(DEFAULT_TAG, message)
    }

    fun e(message: String, throwable: Throwable) {
        e(DEFAULT_TAG, message, throwable)
    }

    fun e(tag: String, message: String) {
        Log.e(tag, makeMessage(message))
    }

    fun e(tag: String, message: String, throwable: Throwable) {
        Log.e(tag, makeMessage(message), throwable)
    }

    fun stackTrace(message: String) {
        stackTrace(DEFAULT_TAG, message)
    }

    fun stackTrace(tag: String, message: String) {
        e(tag, "[Stack Trace] - $message")
        val elements = Thread.currentThread().stackTrace
        for (i in 5 until elements.size) {
            val element = elements[i]
            val methodName = getMethodName(element)
            d(tag, "at ${element.className}.$methodName(${element.fileName}:${element.lineNumber})")
        }
    }

    private fun makeMessage(message: String): String {
        return if (usePrefix) {
            "${prefix()}${message}"
        } else {
            message
        }
    }

    private fun prefix(pos: Int = REAL_METHOD_POS): String {
        val ste = Throwable().stackTrace
        var check = pos
        var realMethod: StackTraceElement? = ste.getOrNull(check)
        while (realMethod != null && (realMethod.fileName == null || isExceptFile(realMethod.fileName))) {
            check++
            realMethod = if (check < ste.size) ste[check] else null
        }
        if (realMethod == null) {
            return ""
        }
        val methodName = getMethodName(realMethod)
        return "[${realMethod.fileName}:${realMethod.lineNumber}:${methodName}()]"
    }

    private fun getMethodName(element: StackTraceElement): String {
        return if (element.methodName.startsWith("lambda$")) {
            "lambda$" + element.methodName.split("$").getOrElse(1) { "" }
        } else {
            element.methodName
        }
    }
}