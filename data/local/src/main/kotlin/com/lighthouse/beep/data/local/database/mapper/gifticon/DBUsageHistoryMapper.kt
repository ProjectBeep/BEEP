package com.lighthouse.beep.data.local.database.mapper.gifticon

import com.lighthouse.beep.data.local.database.entity.DBUsageHistoryEntity
import com.lighthouse.beep.model.user.UsageHistory

internal fun List<DBUsageHistoryEntity>.toModel(): List<UsageHistory> {
    return map {
        it.toModel()
    }
}

internal fun DBUsageHistoryEntity.toModel(): UsageHistory {
    return UsageHistory(
        date = date,
        x = x,
        y = y,
        amount = amount,
    )
}
