package com.lighthouse.beep.data.local.repository.gifticon

import com.lighthouse.beep.data.local.database.dao.GifticonDao
import com.lighthouse.beep.data.local.database.mapper.gifticon.toEntity
import com.lighthouse.beep.data.local.database.mapper.gifticon.toEntityForCreate
import com.lighthouse.beep.data.local.database.mapper.gifticon.toModel
import com.lighthouse.beep.data.model.gifticon.GifticonResource
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonDataSource
import com.lighthouse.beep.model.brand.BrandCategory
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonImageData
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

internal class LocalGifticonDataSourceImpl @Inject constructor(
    private val gifticonDao: GifticonDao,
) : LocalGifticonDataSource {

    override fun isExistGifticon(userId: String, isUsed: Boolean): Flow<Boolean> {
        return gifticonDao.isExistGifticon(userId, isUsed)
    }

    override fun getGifticonCount(userId: String, isUsed: Boolean): Flow<Int> {
        return gifticonDao.getGifticonCount(userId, isUsed)
    }

    override fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): Flow<GifticonDetail?> {
        return gifticonDao.getGifticonDetail(userId, gifticonId).map{ it?.toModel() }
    }

    override suspend fun getGifticonEditInfo(userId: String, gifticonId: Long): GifticonEditInfo? {
        return gifticonDao.getGifticonEditInfo(userId, gifticonId)?.toModel()
    }

    override suspend fun getGifticonResourceList(userId: String, gifticonIdList: List<Long>): List<GifticonResource> {
        return gifticonDao.getGifticonResourceList(userId, gifticonIdList).toModel()
    }

    override suspend fun getGifticonResourceList(userId: String): List<GifticonResource> {
        return gifticonDao.getGifticonResourceList(userId).toModel()
    }

    override fun getGifticonList(
        userId: String,
        isUsed: Boolean,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean
    ): Flow<List<GifticonListItem>> {
        val sortByString = when (gifticonSortBy) {
            com.lighthouse.beep.model.gifticon.GifticonSortBy.RECENT -> "recent"
            com.lighthouse.beep.model.gifticon.GifticonSortBy.DEADLINE -> "expire"
            com.lighthouse.beep.model.gifticon.GifticonSortBy.UPDATE -> "name"
        }
        return gifticonDao.getGifticonList(userId, isUsed, sortByString, if (isAsc) 1 else 0)
            .map { it.toModel() }
    }

    override fun getGifticonListByBrand(
        userId: String,
        brand: String,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean
    ): Flow<List<GifticonListItem>> {
        val sortByString = when (gifticonSortBy) {
            com.lighthouse.beep.model.gifticon.GifticonSortBy.RECENT -> "recent"
            com.lighthouse.beep.model.gifticon.GifticonSortBy.DEADLINE -> "expire"
            com.lighthouse.beep.model.gifticon.GifticonSortBy.UPDATE -> "name"
        }
        return gifticonDao.getGifticonListByBrand(
            userId,
            brand,
            sortByString,
            if (isAsc) 1 else 0
        ).map { it.toModel() }
    }

    override suspend fun getGifticonImageDataList(userId: String): List<GifticonImageData> {
        return gifticonDao.getGifticonImageDataList(userId).toModel()
    }

    override suspend fun insertGifticonList(
        userId: String,
        editInfoList: List<GifticonEditInfo>,
    ): List<Long> {
        return gifticonDao.insertGifticonList(editInfoList.toEntityForCreate(userId))
    }

    override suspend fun updateGifticon(
        gifticonId: Long,
        editInfo: GifticonEditInfo
    ) {
        gifticonDao.updateGifticon(editInfo.toEntity(gifticonId))
    }

    override suspend fun deleteGifticon(userId: String) {
        gifticonDao.deleteGifticon(userId)
    }

    override suspend fun deleteGifticon(
        userId: String,
        gifticonIdList: List<Long>,
    ) {
        gifticonDao.deleteGifticon(userId, gifticonIdList)
    }

    override suspend fun transferGifticon(
        oldUserId: String,
        newUserId: String,
    ) {
        gifticonDao.transferGifticon(oldUserId, newUserId)
    }

    override fun getBrandCategoryList(userId: String): Flow<List<BrandCategory>> {
        return gifticonDao.getBrandCategoryList(userId).map {
            it.toModel()
        }
    }

    override suspend fun useGifticonList(userId: String, gifticonIdList: List<Long>) {
        gifticonDao.useGifticonList(userId, gifticonIdList, Date())
    }

    override suspend fun revertGifticonList(userId: String, gifticonIdList: List<Long>) {
        gifticonDao.revertGifticonList(userId, gifticonIdList)
    }

    override suspend fun updateGifticonUseInfo(
        userId: String,
        gifticonId: Long,
        isUsed: Boolean,
        remain: Int,
    ) {
        gifticonDao.updateGifticonUseInfo(userId, gifticonId, isUsed, remain)
    }
}