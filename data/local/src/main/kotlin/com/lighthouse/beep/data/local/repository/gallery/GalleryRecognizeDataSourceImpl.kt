package com.lighthouse.beep.data.local.repository.gallery

import com.lighthouse.beep.data.local.database.dao.GalleryImageDataDao
import com.lighthouse.beep.data.local.database.entity.DBGalleryImageDataEntity
import com.lighthouse.beep.data.local.database.mapper.gallery.toModel
import com.lighthouse.beep.data.repository.gallery.GalleryRecognizeDataSource
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageData
import javax.inject.Inject

internal class GalleryRecognizeDataSourceImpl @Inject constructor(
    private val dao: GalleryImageDataDao,
): GalleryRecognizeDataSource {

    override suspend fun getRecognizeDataList(): List<GalleryImageData> {
        return dao.getGalleryImageDataList().toModel()
    }

    override suspend fun saveRecognizeData(data: GalleryImage, isGifticon: Boolean) {
        dao.insertGalleryImageData(DBGalleryImageDataEntity(
            data.imagePath,
            data.dateAdded,
            isGifticon,
            null,
        ))
    }
}