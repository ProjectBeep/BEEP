package com.lighthouse.beep.data.local.repository.gifticon

import com.lighthouse.beep.data.local.database.dao.GifticonDao
import com.lighthouse.beep.data.local.database.mapper.gifticon.toEntity
import com.lighthouse.beep.data.local.database.mapper.gifticon.toEntityForCreate
import com.lighthouse.beep.data.local.database.mapper.gifticon.toModel
import com.lighthouse.beep.data.model.gifticon.GifticonResource
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonDataSource
import com.lighthouse.beep.model.brand.BrandCategory
import com.lighthouse.beep.model.exception.db.UpdateException
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import com.lighthouse.beep.model.gifticon.GifticonType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LocalGifticonDataSourceImpl @Inject constructor(
    private val gifticonDao: GifticonDao,
) : LocalGifticonDataSource {

    override fun isExistGifticon(userId: String, isUsed: Boolean): Flow<Boolean> {
        return gifticonDao.isExistGifticon(userId, isUsed)
    }

    override suspend fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): GifticonDetail? {
        return gifticonDao.getGifticonDetail(userId, gifticonId)?.toModel()
    }

    override suspend fun getGifticonResourceList(userId: String, gifticonIdList: List<Long>): List<GifticonResource>? {
        return gifticonDao.getGifticonResourceList(userId, gifticonIdList)?.toModel()
    }

    override suspend fun getGifticonResourceList(userId: String): List<GifticonResource>? {
        return gifticonDao.getGifticonResourceList(userId)?.toModel()
    }

    override fun getGifticonList(
        userId: String,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean
    ): Flow<List<GifticonListItem>> {
        return gifticonDao.getGifticonList(userId, gifticonSortBy.code, if (isAsc) 1 else 0)
            .map { it.toModel() }
    }

    override fun getGifticonListByBrand(
        userId: String,
        brand: String,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean
    ): Flow<List<GifticonListItem>> {
        return gifticonDao.getGifticonListByBrand(
            userId,
            brand,
            gifticonSortBy.code,
            if (isAsc) 1 else 0
        ).map { it.toModel() }
    }

    override fun getUsedGifticonList(userId: String): Flow<List<GifticonListItem>> {
        return gifticonDao.getUsedGifticonList(userId)
            .map { it.toModel() }
    }

    override suspend fun insertGifticonList(
        userId: String,
        editInfo: GifticonEditInfo,
    ) {
        gifticonDao.insertGifticon(editInfo.toEntityForCreate(userId))
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

    override suspend fun useGifticon(userId: String, gifticonId: Long) {
        gifticonDao.useGifticon(userId, gifticonId)
    }

    override suspend fun useGifticonList(userId: String, gifticonIdList: List<Long>) {
        gifticonDao.useGifticonList(userId, gifticonIdList)
    }

    override suspend fun useCashGifticon(
        userId: String,
        gifticonId: Long,
        cash: GifticonType.Cash,
        amount: Int
    ) {
        when {
            cash.remain < amount -> throw UpdateException("사용할 금액이 잔액보다 많습니다.")
            cash.remain == amount -> gifticonDao.useGifticon(userId, gifticonId)
            else -> gifticonDao.useCashGifticon(userId, gifticonId, cash.remain - amount)
        }
    }

    override suspend fun revertUsedGifticon(
        userId: String,
        gifticonId: Long
    ) {
        gifticonDao.revertUsedGifticon(userId, gifticonId)
    }
}