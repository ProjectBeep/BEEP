package com.lighthouse.beep.data.repository.gifticon

import com.lighthouse.beep.data.model.gifticon.GifticonResource
import com.lighthouse.beep.model.brand.BrandCategory
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import com.lighthouse.beep.model.gifticon.GifticonType
import kotlinx.coroutines.flow.Flow

interface LocalGifticonDataSource {

    fun isExistGifticon(
        userId: String,
        isUsed: Boolean,
    ): Flow<Boolean>

    suspend fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): GifticonDetail?

    suspend fun getGifticonResourceList(
        userId: String,
        gifticonIdList: List<Long>,
    ): List<GifticonResource>

    suspend fun getGifticonResourceList(
        userId: String
    ): List<GifticonResource>

    fun getGifticonList(
        userId: String,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean
    ): Flow<List<GifticonListItem>>

    fun getGifticonListByBrand(
        userId: String,
        brand: String,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean
    ): Flow<List<GifticonListItem>>

    fun getUsedGifticonList(userId: String): Flow<List<GifticonListItem>>

    suspend fun insertGifticonList(
        userId: String,
        editInfoList: List<GifticonEditInfo>,
    ): List<Long>

    suspend fun updateGifticon(
        gifticonId: Long,
        editInfo: GifticonEditInfo
    )

    suspend fun deleteGifticon(userId: String)

    suspend fun deleteGifticon(
        userId: String,
        gifticonIdList: List<Long>,
    )

    suspend fun transferGifticon(
        oldUserId: String,
        newUserId: String,
    )

    fun getBrandCategoryList(userId: String): Flow<List<BrandCategory>>

    suspend fun useGifticon(
        userId: String,
        gifticonId: Long,
    )

    suspend fun useGifticonList(
        userId: String,
        gifticonIdList: List<Long>,
    )

    suspend fun useCashGifticon(
        userId: String,
        gifticonId: Long,
        cash: GifticonType.Cash,
        amount: Int
    )

    suspend fun revertUsedGifticon(
        userId: String,
        gifticonId: Long,
    )
}
