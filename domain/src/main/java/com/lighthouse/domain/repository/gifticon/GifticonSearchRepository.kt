package com.lighthouse.domain.repository.gifticon

import com.lighthouse.beep.model.brand.BrandWithGifticonCount
import com.lighthouse.beep.model.etc.SortBy
import com.lighthouse.beep.model.gifticon.Gifticon
import com.lighthouse.beep.model.gifticon.GifticonNotification
import com.lighthouse.beep.model.gifticon.GifticonWithCrop
import kotlinx.coroutines.flow.Flow

interface GifticonSearchRepository {

    fun getGifticonsWithLimit(
        userId: String,
        limit: Int
    ): Flow<List<Gifticon>>

    fun getGifticonsWithDDay(
        userId: String,
        dDaySet: Set<Int>
    ): Flow<List<GifticonNotification>>

    // ////////////////////////////////////////////////////

    fun getGifticon(
        userId: String,
        gifticonId: String
    ): Flow<Gifticon>

    fun getGifticons(
        userId: String,
        count: Int
    ): Flow<List<Gifticon>>

    fun getAllGifticons(
        userId: String,
        isUsed: Boolean,
        filterExpired: Boolean,
        sortBy: SortBy = SortBy.DEADLINE
    ): Flow<List<Gifticon>>

    fun getFilteredGifticons(
        userId: String,
        isUsed: Boolean,
        filterBrand: Set<String>,
        filterExpired: Boolean,
        sortBy: SortBy = SortBy.DEADLINE
    ): Flow<List<Gifticon>>

    fun getGifticonByBrand(
        userId: String,
        isUsed: Boolean,
        brand: String,
        filterExpired: Boolean
    ): Flow<List<Gifticon>>

    fun getAllBrands(
        userId: String,
        isUsed: Boolean,
        filterExpired: Boolean
    ): Flow<List<BrandWithGifticonCount>>

    suspend fun getGifticonCrop(
        userId: String,
        gifticonId: String
    ): Result<GifticonWithCrop>

    fun hasGifticon(
        userId: String,
        isUsed: Boolean,
        filterExpired: Boolean
    ): Flow<Boolean>

    suspend fun hasGifticonBrand(
        userId: String,
        brand: String
    ): Result<Boolean>

    fun getGifticonBrands(
        userId: String
    ): Flow<List<String>>
}
