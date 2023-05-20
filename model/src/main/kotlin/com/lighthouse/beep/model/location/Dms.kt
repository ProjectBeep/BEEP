package com.lighthouse.beep.model.location

import kotlin.math.floor

/**
 * 정확히 받아오는 데이터는 dd 이기 때문에 가능 하면 dd 기준으로 데이터를 저장하고 불러오기
 *
 * @property degree 도
 * @property minutes 분
 * @property seconds 초
 */
data class Dms(
    val degree: Int,
    val minutes: Int,
    val seconds: Int,
    val dd: Double,
) {
    private constructor(degree: Int, minutes: Int, dd: Double) : this(
        degree,
        minutes,
        floor(((dd - degree) * 60 - minutes) * 60).toInt(),
        dd,
    )

    private constructor(degree: Int, dd: Double) : this(
        degree,
        floor((dd - degree) * 60).toInt(),
        dd,
    )

    constructor(dd: Double) : this(
        floor(dd).toInt(),
        dd,
    )

    constructor(degree: Int = 0, minutes: Int = 0, seconds: Int = 0) : this(
        degree,
        minutes,
        seconds,
        degree + minutes.toDouble() / 60 + seconds.toDouble() / 3600,
    )

    val formatDms = "%d%02d%02d".format(degree, minutes, seconds)

    val radian: Double = dd * Math.PI / 180.0

    operator fun plus(dms: Dms): Dms {
        val sum =
            (degree + dms.degree) * 3600 + (minutes + dms.minutes) * 60 + seconds + dms.seconds
        return Dms(sum / 3600, (sum - (sum % 3600)) / 60, sum % 60)
    }

    fun sampling(dGap: Int = 0, mGap: Int = 0, sGap: Int = 0): Dms {
        val dOffset = if (dGap != 0) degree % dGap else 0
        val mOffset = if (mGap != 0) minutes % mGap else 0
        val sOffset = if (sGap != 0) seconds % sGap else 0
        return plus(Dms(-dOffset, -mOffset, -sOffset))
    }

    companion object {
        val NULL = Dms(0, 0, 0, 0.0)
    }
}
