package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lighthouse.beep.data.local.database.entity.DBGalleryImageDataEntity

@Dao
internal interface GalleryImageDataDao {

    @Query("SELECT * FROM gallery_image_data")
    suspend fun getGalleryImageDataList(): List<DBGalleryImageDataEntity>

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertGalleryImageData(galleryRecognize: DBGalleryImageDataEntity)
}