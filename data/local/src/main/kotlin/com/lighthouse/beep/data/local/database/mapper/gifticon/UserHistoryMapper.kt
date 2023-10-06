package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.entity.DBUsageHistoryEntity
import com.lighthouse.beep.model.user.UsageHistory

internal fun UsageHistory.toEntity(gifticonId: Long): DBUsageHistoryEntity {
    return DBUsageHistoryEntity(
        id = null,
        gifticonId = gifticonId,
        date = date,
        x = x,
        y = y,
        amount = amount,
    )
}
