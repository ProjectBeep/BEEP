package com.lighthouse.beep.data.repository.gifticon

import com.lighthouse.beep.model.brand.BrandCategory
import com.lighthouse.beep.model.gifticon.GifticonDetail
import com.lighthouse.beep.model.gifticon.GifticonEditInfo
import com.lighthouse.beep.model.gifticon.GifticonListItem
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GifticonRepositoryImpl @Inject constructor(
    private val localGifticonDataSource: LocalGifticonDataSource,
    private val gifticonStorage: LocalGifticonStorage,
) : GifticonRepository {

    override fun isExistGifticon(userId: String, isUsed: Boolean): Flow<Boolean> {
        return localGifticonDataSource.isExistGifticon(userId, isUsed)
    }

    override fun getGifticonCount(userId: String, isUsed: Boolean): Flow<Int> {
        return localGifticonDataSource.getGifticonCount(userId, isUsed)
    }

    override fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): Flow<GifticonDetail?> {
        return localGifticonDataSource.getGifticonDetail(userId, gifticonId)
    }

    override fun getGifticonList(
        userId: String,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean
    ): Flow<List<GifticonListItem>> {
        return localGifticonDataSource.getGifticonList(userId, gifticonSortBy, isAsc)
    }

    override fun getGifticonListByBrand(
        userId: String,
        brand: String,
        gifticonSortBy: GifticonSortBy,
        isAsc: Boolean
    ): Flow<List<GifticonListItem>> {
        return localGifticonDataSource.getGifticonListByBrand(userId, brand.lowercase(), gifticonSortBy, isAsc)
    }

    override fun getUsedGifticonList(userId: String): Flow<List<GifticonListItem>> {
        return localGifticonDataSource.getUsedGifticonList(userId)
    }

    override suspend fun insertGifticonList(
        userId: String,
        gifticonInfoList: List<GifticonEditInfo>,
    ): List<Long> {
        val editInfoList = gifticonInfoList.map {
            val originResult = gifticonStorage.saveGifticonOriginImage(it.originUri)
            val thumbnail = it.thumbnailBitmap
            if (thumbnail != null) {
                val thumbnailUri = gifticonStorage.saveGifticonThumbnailImage(thumbnail)
                it.copy(
                    gifticonUri = originResult.gifticonUri,
                    thumbnailUri = thumbnailUri,
                )
            } else {
                val thumbnailResult =
                    gifticonStorage.cropAndSaveGifticonThumbnailImage(originResult.gifticonBitmap)
                it.copy(
                    gifticonUri = originResult.gifticonUri,
                    thumbnailUri = thumbnailResult.thumbnailUri,
                    thumbnailRect = thumbnailResult.thumbnailRect,
                )
            }
        }
        return localGifticonDataSource.insertGifticonList(userId, editInfoList)
    }

    override suspend fun updateGifticon(
        gifticonId: Long,
        gifticonInfo: GifticonEditInfo
    ): Result<Unit> = runCatching {
        val thumbnail = gifticonInfo.thumbnailBitmap
        var editInfo = gifticonInfo
        if (thumbnail != null) {
            gifticonStorage.deleteFile(editInfo.thumbnailUri)
            editInfo = editInfo.copy(
                thumbnailUri = gifticonStorage.saveGifticonThumbnailImage(thumbnail)
            )
        }
        localGifticonDataSource.updateGifticon(gifticonId, editInfo)
    }

    override suspend fun deleteGifticon(userId: String): Result<Unit> = runCatching {
        localGifticonDataSource.getGifticonResourceList(userId).forEach {
            gifticonStorage.deleteFile(it.gifticonUri)
            gifticonStorage.deleteFile(it.thumbnailUri)
        }
        localGifticonDataSource.deleteGifticon(userId)
    }

    override suspend fun deleteGifticon(
        userId: String,
        gifticonIdList: List<Long>,
    ): Result<Unit> = runCatching {
        localGifticonDataSource.getGifticonResourceList(userId, gifticonIdList).forEach {
            gifticonStorage.deleteFile(it.gifticonUri)
            gifticonStorage.deleteFile(it.thumbnailUri)
        }
        localGifticonDataSource.deleteGifticon(userId, gifticonIdList)
    }

    override suspend fun transferGifticon(
        oldUserId: String,
        newUserId: String,
    ): Result<Unit> = runCatching {
        localGifticonDataSource.transferGifticon(oldUserId, newUserId)
    }

    override fun getBrandCategoryList(userId: String): Flow<List<BrandCategory>> {
        return localGifticonDataSource.getBrandCategoryList(userId)
    }

    override suspend fun useGifticonList(
        userId: String,
        gifticonIdList: List<Long>,
    ): Result<Unit> = runCatching {
        localGifticonDataSource.useGifticonList(userId, gifticonIdList)
    }

    override suspend fun updateGifticonUseInfo(
        userId: String,
        gifticonId: Long,
        isUsed: Boolean,
        remain: Int,
    ): Result<Unit> = runCatching {
        localGifticonDataSource.updateGifticonUseInfo(userId, gifticonId, isUsed, remain)
    }
}
