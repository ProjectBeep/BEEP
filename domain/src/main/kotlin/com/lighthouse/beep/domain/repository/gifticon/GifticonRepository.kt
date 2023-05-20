package com.lighthouse.beep.domain.repository.gifticon

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

interface GifticonRepository {

    suspend fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): Result<GifticonDetail>

    suspend fun insertGifticonList(
        userId: String,
        gifticonForAdditionList: List<GifticonForAddition>,
    ): Result<Unit>

    suspend fun updateGifticon(
        gifticonForUpdate: GifticonForUpdate,
    ): Result<Unit>

    suspend fun deleteGifticon(
        userId: String,
        gifticonId: Long,
    ): Result<Unit>

    suspend fun transferGifticon(
        oldUserId: String,
        newUserId: String,
    ): Result<Unit>

    suspend fun hasGifticonBrand(
        userId: String,
        brand: String,
    ): Result<Boolean>

    suspend fun getGifticonList(
        userId: String,
        isUsed: Boolean,
        updatedAt: Date,
        excludeExpire: Boolean,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean,
        offset: Int,
        limit: Int = 10,
    ): List<GifticonListItem>

    suspend fun getGifticonNotifications(
        userId: String,
        dDaySet: Set<Int>,
    ): List<GifticonNotification>

    suspend fun getGifticonByBrand(
        userId: String,
        filters: Set<String>,
        isUsed: Boolean,
        updatedAt: Date,
        excludeExpire: Boolean,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean,
        offset: Int,
        limit: Int = 10,
    ): List<GifticonListItem>

    fun getBrandCategoryList(
        userId: String,
        isUsed: Boolean,
        excludeExpire: Boolean,
    ): Flow<List<BrandCategory>>

    suspend fun useGifticon(
        userId: String,
        gifticonId: Long,
        usageHistory: UsageHistory,
    ): Result<Unit>

    suspend fun useCashCardGifticon(
        userId: String,
        gifticonId: Long,
        amount: Int,
        usageHistory: UsageHistory,
    ): Result<Unit>

    suspend fun revertUsedGifticon(
        userId: String,
        gifticonId: Long,
    ): Result<Unit>

    fun getUsageHistory(
        userId: String,
        gifticonId: Long,
    ): Flow<List<UsageHistory>>

    suspend fun insertUsageHistory(
        gifticonId: Long,
        usageHistory: UsageHistory,
    ): Result<Unit>
}
