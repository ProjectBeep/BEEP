package com.lighthouse.beep.model.location

data class DmsRect(
    val left: Dms,
    val top: Dms,
    val right: Dms,
    val bottom: Dms,
) {
    val format = "${left.dd},${top.dd},${right.dd},${bottom.dd}"
}
