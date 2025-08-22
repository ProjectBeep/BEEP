package com.lighthouse.beep.model.location

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin

/**
 * lat : x
 * lon : y
 */
data class DmsPos(
    val lat: Dms,
    val lon: Dms,
) {
    constructor(latDD: Double, lonDD: Double) : this(Dms(latDD), Dms(lonDD))

    fun distance(lat: Dms, lon: Dms): Double {
        return calculateDistance(this.lat, this.lon, lat, lon)
    }

    fun distance(other: DmsPos): Double {
        return calculateDistance(lat, lon, other.lat, other.lon)
    }

    // 참고 : https://www.geodatasource.com/developers/java
    // m 단위의 거리를 반환 한다
    private fun calculateDistance(lat1: Dms, lon1: Dms, lat2: Dms, lon2: Dms): Double {
        val theta = (lon1.dd - lon2.dd) * Math.PI / 180.0
        return acos(
            sin(lat1.radian) * sin(lat2.radian) +
                cos(lat1.radian) * cos(lat2.radian) * cos(theta),
        ) * 180 / Math.PI * 60 * 1.1515 * 1609.344
    }
}
