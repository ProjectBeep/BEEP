package com.lighthouse.beep.data.local.repository.gifticon

import androidx.room.withTransaction
import com.lighthouse.beep.data.local.database.BeepDatabase
import com.lighthouse.beep.data.local.database.dao.GalleryImageDataDao
import com.lighthouse.beep.data.local.database.dao.GifticonDao
import com.lighthouse.beep.data.local.database.mapper.gifticon.toEntity
import com.lighthouse.beep.data.local.database.mapper.gifticon.toEntityForCreate
import com.lighthouse.beep.data.local.database.mapper.gifticon.toModel
import com.lighthouse.beep.data.model.gifticon.GifticonResource
import com.lighthouse.beep.data.repository.gifticon.LocalGifticonDataSource
import com.lighthouse.beep.model.brand.BrandCategory
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

internal class LocalGifticonDataSourceImpl @Inject constructor(
    private val db: BeepDatabase,
    private val gifticonDao: GifticonDao,
    private val galleryImageDataDao: GalleryImageDataDao,
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
        return gifticonDao.getGifticonList(userId, isUsed, gifticonSortBy.code, if (isAsc) 1 else 0)
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
        editInfoList: List<GifticonEditInfo>,
    ): List<Long> {
        return db.withTransaction {
            editInfoList.map {
                val gifticonId = gifticonDao.insertGifticon(it.toEntityForCreate(userId))
                galleryImageDataDao.updateAddedGifticonId(
                    gifticonId = gifticonId,
                    imagePath = it.imagePath,
                    addedData = it.imageAddedDate,
                )
                gifticonId
            }
        }
    }

    override suspend fun updateGifticon(
        gifticonId: Long,
        editInfo: GifticonEditInfo
    ) {
        gifticonDao.updateGifticon(editInfo.toEntity(gifticonId))
    }

    override suspend fun deleteGifticon(userId: String) {
        db.withTransaction {
            val gifticonIdList = gifticonDao.getGifticonIdList(userId)
            galleryImageDataDao.deleteAddedGifticonId(gifticonIdList)
            gifticonDao.deleteGifticon(userId)
        }
    }

    override suspend fun deleteGifticon(
        userId: String,
        gifticonIdList: List<Long>,
    ) {
        db.withTransaction {
            galleryImageDataDao.deleteAddedGifticonId(gifticonIdList)
            gifticonDao.deleteGifticon(userId, gifticonIdList)
        }
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
        gifticonDao.revertGifticonList(userId, gifticonIdList, Date())
    }

    override suspend fun updateGifticonUseInfo(
        userId: String,
        gifticonId: Long,
        isUsed: Boolean,
        remain: Int,
    ) {
        gifticonDao.updateGifticonUseInfo(userId, gifticonId, isUsed, remain, Date())
    }
}