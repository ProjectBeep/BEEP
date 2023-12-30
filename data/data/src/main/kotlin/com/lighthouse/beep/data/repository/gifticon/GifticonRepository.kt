package com.lighthouse.beep.data.repository.gifticon

import com.lighthouse.beep.model.brand.BrandCategory
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import kotlinx.coroutines.flow.Flow

interface GifticonRepository {

    fun isExistGifticon(
        userId: String,
        isUsed: Boolean,
    ): Flow<Boolean>

    fun getGifticonCount(
        userId: String,
        isUsed: Boolean,
    ): Flow<Int>

    fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): Flow<GifticonDetail?>

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
    ): List<Long>

    suspend fun updateGifticon(
        gifticonId: Long,
        gifticonInfo: GifticonEditInfo,
    ): Result<Unit>

    suspend fun deleteGifticon(userId: String): Result<Unit>

    suspend fun deleteGifticon(
        userId: String,
        gifticonIdList: List<Long>,
    ): Result<Unit>

    suspend fun transferGifticon(
        oldUserId: String,
        newUserId: String,
    ): Result<Unit>

    fun getBrandCategoryList(userId: String): Flow<List<BrandCategory>>

    suspend fun useGifticonList(
        userId: String,
        gifticonIdList: List<Long>,
    ): Result<Unit>

    suspend fun updateGifticonUseInfo(
        userId: String,
        gifticonId: Long,
        isUsed: Boolean,
        remain: Int,
    ): Result<Unit>
}
