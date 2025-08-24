package com.lighthouse.beep.core.common.utils.log

import android.annotation.SuppressLint

class MSTimeChecker(
    private val log: MSLogger,
) {
    companion object {
        private const val TAG = "time_checker"

        private const val SECOND = 1000L
        private const val MINUTE = 60 * SECOND
        private const val HOUR = 60 * MINUTE
        private const val DATE = 24 * HOUR
    }

    private val timeMap = mutableMapOf<String, Long>()
    private val firstTimeMap = mutableMapOf<String, Long>()

    fun tick(tag: String, message: String) {
        if (message.isNotEmpty()) {
            log.d(TAG, "Tick[$tag] - $message")
        } else {
            log.d(TAG, "Tick[$tag]")
        }

        val currentTime = System.currentTimeMillis()
        val firstTick = firstTimeMap[tag]
        val oldTick = timeMap[tag]

        if (firstTick == null) {
            firstTimeMap[tag] = currentTime
        }
        timeMap[tag] = currentTime

        if (firstTick != null && oldTick != null) {
            log.e(TAG, "동일한 Tag[$tag]가 들어와 시간을 갱신합니다")
            if (firstTick == oldTick) {
                log.e(TAG, "Before ~ [${formatedTime(currentTime - oldTick)}]")
            } else {
                log.e(TAG, "Before ~ [${formatedTime(currentTime - oldTick)}], First ~[${formatedTime(currentTime - firstTick)}]")
            }
        }
    }

    fun tock(tag: String, message: String) {
        val currentTime = System.currentTimeMillis()
        val oldTick = timeMap[tag]
        if (oldTick == null) {
            log.e(TAG, "일치하는 Tag[$tag]가 없습니다.")
            return
        }

        firstTimeMap.remove(tag)
        timeMap.remove(tag)

        val tockTime = currentTime - oldTick
        if (message.isNotEmpty()) {
            log.d(TAG, "Tock[$tag](${formatedTime(tockTime)}) - $message")
        } else {
            log.d(TAG, "Tock[$tag](${formatedTime(tockTime)})")
        }
    }


    @SuppressLint("DefaultLocale")
    private fun formatedTime(time: Long): String {
        val date = time / DATE
        val hour = String.format("%02d", (time % DATE) / HOUR)
        val minute = String.format("%02d",(time % HOUR) / MINUTE)
        val second = String.format("%02d",(time % MINUTE) / SECOND)
        val millisecond = String.format("%03d",time % SECOND)
        return "$date $hour:$minute:$second.$millisecond"
    }
}