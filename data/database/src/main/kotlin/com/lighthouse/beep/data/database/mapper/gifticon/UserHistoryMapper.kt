package com.lighthouse.beep.data.database.mapper.gifticon

import com.lighthouse.beep.data.database.entity.DBUsageHistoryEntity
import com.lighthouse.beep.model.user.UsageHistory

internal fun UsageHistory.toEntity(gifticonId: Long): DBUsageHistoryEntity {
    return DBUsageHistoryEntity(
        gifticonId = gifticonId,
        date = date,
        x = x,
        y = y,
        amount = amount,
    )
}