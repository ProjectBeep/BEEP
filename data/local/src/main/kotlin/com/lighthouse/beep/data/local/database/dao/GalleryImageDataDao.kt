package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lighthouse.beep.data.local.database.entity.DBGalleryImageDataEntity

@Dao
internal interface GalleryImageDataDao {

    @Query("SELECT * FROM gallery_images_table ORDER BY date_added DESC")
    suspend fun getGalleryImageDataList(): List<DBGalleryImageDataEntity>

    @Insert
    suspend fun insertGalleryImageData(data: DBGalleryImageDataEntity)

    @Query("DELETE FROM gallery_images_table WHERE image_path = :imagePath")
    suspend fun deleteGalleryImageData(imagePath: String)

    @Query("DELETE FROM gallery_images_table")
    suspend fun clearAllGalleryImageData()
}