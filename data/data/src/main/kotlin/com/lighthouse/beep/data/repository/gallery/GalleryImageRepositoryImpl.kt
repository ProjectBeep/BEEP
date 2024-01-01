package com.lighthouse.beep.data.repository.gallery

import androidx.paging.PagingData
import com.lighthouse.beep.model.gallery.GalleryImage
import com.lighthouse.beep.model.gallery.GalleryImageRecognizeData
import com.lighthouse.beep.model.gallery.GalleryRecognize
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GalleryImageRepositoryImpl @Inject constructor(
    private val dataSource: GalleryImageDataSource,
    private val galleryDataSource: GalleryRecognizeDataSource,
) : GalleryImageRepository {

    override suspend fun getImage(id: Long): GalleryImage? {
        return dataSource.getImage(id)
    }

    override fun getImages(pageSize: Int): Flow<PagingData<GalleryImage>> {
        return dataSource.getImages(pageSize)
    }

    override suspend fun getImages(page: Int, limit: Int, offset: Int): List<GalleryImage> {
        return dataSource.getImages(page, limit, offset)
    }

    override fun getImageSize(): Int {
        return dataSource.getImageSize()
    }

    override suspend fun getRecognizeData(data: GalleryImage): GalleryImageRecognizeData {
        return galleryDataSource.getRecognizeData(data)
    }

    override suspend fun getRecognizeDataList(): List<GalleryRecognize> {
        return galleryDataSource.getRecognizeDataList()
    }

    override suspend fun saveRecognizeData(data: GalleryImage, isGifticon: Boolean) {
        galleryDataSource.saveRecognizeData(data, isGifticon)
    }
}
