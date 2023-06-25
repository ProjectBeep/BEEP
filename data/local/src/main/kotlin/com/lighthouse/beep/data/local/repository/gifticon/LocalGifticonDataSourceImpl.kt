package com.lighthouse.beep.data.local.repository.gifticon

import com.lighthouse.beep.data.local.database.dao.GifticonDao
import com.lighthouse.beep.data.local.database.mapper.gifticon.toEntity
import com.lighthouse.beep.data.local.database.mapper.gifticon.toModel
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonDataSource
import com.lighthouse.beep.model.brand.BrandCategory
import com.lighthouse.beep.model.exception.common.NotFoundException
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonForAddition
import com.lighthouse.beep.model.gifticon.GifticonForUpdate
import com.lighthouse.beep.model.gifticon.GifticonImageCreateResult
import com.lighthouse.beep.model.gifticon.GifticonImageUpdateResult
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonNotification
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import com.lighthouse.beep.model.user.UsageHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

internal class LocalGifticonDataSourceImpl @Inject constructor(
    private val gifticonDao: GifticonDao,
) : LocalGifticonDataSource {

    override suspend fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): Result<GifticonDetail> {
        return runCatching {
            gifticonDao.getGifticonDetail(
                userId,
                gifticonId,
            )?.toModel() ?: throw NotFoundException("해당 기프티콘을 찾을 수 없습니다.")
        }
    }

    override suspend fun insertGifticon(
        userId: String,
        gifticonForAddition: GifticonForAddition,
        gifticonImageCreateResult: GifticonImageCreateResult,
    ): Result<Unit> {
        return runCatching {
            gifticonDao.insertGifticon(
                gifticonForAddition.toEntity(
                    userId,
                    gifticonImageCreateResult,
                ),
            )
        }
    }

    override suspend fun updateGifticon(
        gifticonForUpdate: GifticonForUpdate,
        gifticonImageUpdateResult: GifticonImageUpdateResult?,
    ): Result<Unit> {
        return runCatching {
            gifticonDao.updateGifticon(
                gifticonForUpdate.toEntity(
                    gifticonImageUpdateResult,
                ),
            )
        }
    }

    override suspend fun deleteGifticon(
        userId: String,
        gifticonId: Long,
    ): Result<Unit> {
        return runCatching {
            gifticonDao.deleteGifticon(userId, gifticonId)
        }
    }

    override suspend fun transferGifticon(
        oldUserId: String,
        newUserId: String,
    ): Result<Unit> {
        return runCatching {
            gifticonDao.transferGifticon(oldUserId, newUserId)
        }
    }

    override suspend fun hasGifticonBrand(userId: String, brand: String): Result<Boolean> {
        return runCatching {
            gifticonDao.hasGifticonBrand(userId, brand)
        }
    }

    override suspend fun getGifticons(
        userId: String,
        isUsed: Boolean,
        updatedAt: Date,
        excludeExpire: Boolean,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean,
        offset: Int,
        limit: Int,
    ): List<GifticonListItem> {
        return gifticonDao.getGifticonList(
            userId = userId,
            isUsed = isUsed,
            updatedAt = updatedAt,
            excludeExpire = if (excludeExpire) 1 else 0,
            sortCode = gifticonSortBy.ordinal,
            isAsc = if (isAsc) 1 else 0,
            offset = offset,
            limit = limit,
        ).map {
            it.toModel()
        }
    }

    override suspend fun getGifticonNotifications(
        userId: String,
        dDaySet: Set<Int>,
    ): List<GifticonNotification> {
        return gifticonDao.getGifticonNotifications(userId, dDaySet).map {
            it.toModel()
        }
    }

    override suspend fun getGifticonByBrand(
        userId: String,
        filters: Set<String>,
        isUsed: Boolean,
        updatedAt: Date,
        excludeExpire: Boolean,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean,
        offset: Int,
        limit: Int,
    ): List<GifticonListItem> {
        return gifticonDao.getGifticonByBrand(
            userId = userId,
            filters = filters,
            isUsed = isUsed,
            updatedAt = updatedAt,
            excludeExpire = if (excludeExpire) 1 else 0,
            sortCode = gifticonSortBy.ordinal,
            isAsc = if (isAsc) 1 else 0,
            offset = offset,
            limit = limit,
        ).map {
            it.toModel()
        }
    }

    override fun getBrandCategoryList(
        userId: String,
        isUsed: Boolean,
        excludeExpire: Boolean,
    ): Flow<List<BrandCategory>> {
        return gifticonDao.getBrandCategoryList(
            userId,
            if (isUsed) 1 else 0,
            if (excludeExpire) 1 else 0,
            Date(),
        ).map {
            it.toModel()
        }
    }

    override suspend fun useGifticon(
        userId: String,
        gifticonId: Long,
        usageHistory: UsageHistory,
    ): Result<Unit> {
        return runCatching {
            gifticonDao.useGifticonAndInsertHistory(
                userId,
                usageHistory.toEntity(gifticonId),
            )
        }
    }

    override suspend fun useCashCardGifticon(
        userId: String,
        gifticonId: Long,
        amount: Int,
        usageHistory: UsageHistory,
    ): Result<Unit> {
        return runCatching {
            gifticonDao.useCashCardGifticonAndInsertHistory(
                userId,
                amount,
                usageHistory.toEntity(gifticonId),
            )
        }
    }

    override suspend fun revertUsedGifticon(
        userId: String,
        gifticonId: Long,
    ): Result<Unit> {
        return runCatching {
            gifticonDao.revertUsedGifticonAndDeleteHistory(
                userId,
                gifticonId,
            )
        }
    }

    override fun getUsageHistory(
        userId: String,
        gifticonId: Long,
    ): Flow<List<UsageHistory>> {
        return gifticonDao.getUsageHistory(
            userId,
            gifticonId,
        ).map {
            it.toModel()
        }
    }

    override suspend fun insertUsageHistory(
        gifticonId: Long,
        usageHistory: UsageHistory,
    ): Result<Unit> {
        return runCatching {
            gifticonDao.insertUsageHistory(usageHistory.toEntity(gifticonId))
        }
    }
}
