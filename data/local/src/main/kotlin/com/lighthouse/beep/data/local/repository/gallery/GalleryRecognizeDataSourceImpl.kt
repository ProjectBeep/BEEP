package com.lighthouse.beep.data.local.repository.gallery

import com.lighthouse.beep.data.local.database.dao.GalleryRecognizeDao
import com.lighthouse.beep.data.local.database.entity.DBGalleryRecognizeEntity
import com.lighthouse.beep.data.local.database.mapper.gallery.toModel
import com.lighthouse.beep.data.repository.gallery.GalleryRecognizeDataSource
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryRecognize
import javax.inject.Inject

internal class GalleryRecognizeDataSourceImpl @Inject constructor(
    private val dao: GalleryRecognizeDao,
): GalleryRecognizeDataSource {

    override suspend fun getRecognizeDataList(): List<GalleryRecognize> {
        return dao.getRecognizeDataList().toModel()
    }

    override suspend fun saveRecognizeData(data: GalleryImage, isGifticon: Boolean) {
        dao.insertGalleryImageRecognizeData(DBGalleryRecognizeEntity(
            data.imagePath,
            data.dateAdded,
            isGifticon
        ))
    }
}