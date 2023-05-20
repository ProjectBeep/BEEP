package com.lighthouse.beep.model.user

import com.lighthouse.beep.model.location.Dms
import java.util.Date

data class UsageHistory(
    val date: Date,
    val x: Dms,
    val y: Dms,
    val amount: Int,
)
