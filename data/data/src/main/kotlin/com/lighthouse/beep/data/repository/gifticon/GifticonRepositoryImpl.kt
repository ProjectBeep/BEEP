package com.lighthouse.beep.data.repository.gifticon

import com.lighthouse.beep.model.brand.BrandCategory
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonForAddition
import com.lighthouse.beep.model.gifticon.GifticonForUpdate
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonNotification
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import com.lighthouse.beep.model.user.UsageHistory
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

internal class GifticonRepositoryImpl @Inject constructor(
    private val localGifticonDataSource: LocalGifticonDataSource,
    private val gifticonStorage: LocalGifticonStorage,
) : GifticonRepository {

    override suspend fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): Result<GifticonDetail> {
        return localGifticonDataSource.getGifticonDetail(userId, gifticonId)
    }

    override suspend fun insertGifticonList(
        userId: String,
        gifticonForAdditionList: List<GifticonForAddition>,
    ): Result<Unit> = runCatching {
        gifticonForAdditionList.forEach { item ->
            val result = gifticonStorage.createGifticonImage(
                item.originUri,
                item.croppedUri,
                item.croppedRect,
            )
            localGifticonDataSource.insertGifticon(userId, item, result.getOrThrow())
        }
    }

    override suspend fun updateGifticon(
        gifticonForUpdate: GifticonForUpdate,
    ): Result<Unit> = runCatching {
        val result = gifticonStorage.updateGifticonImage(
            gifticonForUpdate.newCroppedUri,
            gifticonForUpdate.newCroppedRect,
        )
        localGifticonDataSource.updateGifticon(gifticonForUpdate, result.getOrThrow())
    }

    override suspend fun deleteGifticon(
        userId: String,
        gifticonId: Long,
    ): Result<Unit> {
        return localGifticonDataSource.deleteGifticon(userId, gifticonId)
    }

    override suspend fun transferGifticon(
        oldUserId: String,
        newUserId: String,
    ): Result<Unit> {
        return localGifticonDataSource.transferGifticon(oldUserId, newUserId)
    }

    override suspend fun hasGifticonBrand(userId: String, brand: String): Result<Boolean> {
        return localGifticonDataSource.hasGifticonBrand(userId, brand)
    }

    override suspend fun getGifticonList(
        userId: String,
        isUsed: Boolean,
        updatedAt: Date,
        excludeExpire: Boolean,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean,
        offset: Int,
        limit: Int,
    ): List<GifticonListItem> {
        return localGifticonDataSource.getGifticons(
            userId,
            isUsed,
            updatedAt,
            excludeExpire,
            gifticonSortBy,
            isAsc,
            offset,
            limit,
        )
    }

    override suspend fun getGifticonNotifications(
        userId: String,
        dDaySet: Set<Int>,
    ): List<GifticonNotification> {
        return localGifticonDataSource.getGifticonNotifications(userId, dDaySet)
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
        return localGifticonDataSource.getGifticonByBrand(
            userId,
            filters,
            isUsed,
            updatedAt,
            excludeExpire,
            gifticonSortBy,
            isAsc,
            offset,
            limit,
        )
    }

    override fun getBrandCategoryList(
        userId: String,
        isUsed: Boolean,
        excludeExpire: Boolean,
    ): Flow<List<BrandCategory>> {
        return localGifticonDataSource.getBrandCategoryList(
            userId,
            isUsed,
            excludeExpire,
        )
    }

    override suspend fun useGifticon(
        userId: String,
        gifticonId: Long,
        usageHistory: UsageHistory,
    ): Result<Unit> {
        return localGifticonDataSource.useGifticon(
            userId,
            gifticonId,
            usageHistory,
        )
    }

    override suspend fun useCashCardGifticon(
        userId: String,
        gifticonId: Long,
        amount: Int,
        usageHistory: UsageHistory,
    ): Result<Unit> {
        return localGifticonDataSource.useCashCardGifticon(
            userId,
            gifticonId,
            amount,
            usageHistory,
        )
    }

    override suspend fun revertUsedGifticon(
        userId: String,
        gifticonId: Long,
    ): Result<Unit> {
        return localGifticonDataSource.revertUsedGifticon(
            userId,
            gifticonId,
        )
    }

    override fun getUsageHistory(
        userId: String,
        gifticonId: Long,
    ): Flow<List<UsageHistory>> {
        return localGifticonDataSource.getUsageHistory(
            userId,
            gifticonId,
        )
    }

    override suspend fun insertUsageHistory(
        gifticonId: Long,
        usageHistory: UsageHistory,
    ): Result<Unit> {
        return localGifticonDataSource.insertUsageHistory(
            gifticonId,
            usageHistory,
        )
    }
}
