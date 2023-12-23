package com.lighthouse.beep.data.repository.gifticon

import com.lighthouse.beep.model.brand.BrandCategory
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import com.lighthouse.beep.model.gifticon.GifticonType
import kotlinx.coroutines.flow.Flow

interface GifticonRepository {

    suspend fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): GifticonDetail?

    fun getGifticonList(
        userId: String,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean,
    ): Flow<List<GifticonListItem>>

    fun getGifticonListByBrand(
        userId: String,
        brand: String,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean,
    ): Flow<List<GifticonListItem>>

    fun getUsedGifticonList(
        userId: String,
    ): Flow<List<GifticonListItem>>

    suspend fun insertGifticonList(
        userId: String,
        gifticonInfoList: List<GifticonEditInfo>,
    ): Result<Unit>

    suspend fun updateGifticon(
        gifticonId: Long,
        gifticonInfo: GifticonEditInfo,
    ): Result<Unit>

    suspend fun deleteGifticon(userId: String): Result<Unit>

    suspend fun deleteGifticon(
        userId: String,
        gifticonId: Long,
    ): Result<Unit>

    suspend fun transferGifticon(
        oldUserId: String,
        newUserId: String,
    ): Result<Unit>

    fun getBrandCategoryList(userId: String): Flow<List<BrandCategory>>

    suspend fun useGifticon(
        userId: String,
        gifticonId: Long,
    ): Result<Unit>

    suspend fun useCashGifticon(
        userId: String,
        gifticonId: Long,
        cash: GifticonType.Cash,
        amount: Int
    ): Result<Unit>

    suspend fun revertUsedGifticon(
        userId: String,
        gifticonId: Long,
    ): Result<Unit>
}
