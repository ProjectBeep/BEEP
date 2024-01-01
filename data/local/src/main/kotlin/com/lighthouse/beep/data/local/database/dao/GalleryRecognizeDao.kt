package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lighthouse.beep.data.local.database.entity.DBGalleryRecognizeEntity
import java.util.Date

@Dao
internal interface GalleryRecognizeDao {

    @Query("SELECT is_gifticon FROM gallery_recognize WHERE image_path = :imagePath AND date_added = :dateAdded")
    suspend fun isGifticon(imagePath: String, dateAdded: Date): Boolean?

    @Query("SELECT * FROM gallery_recognize")
    suspend fun getRecognizeDataList(): List<DBGalleryRecognizeEntity>

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertGalleryImageRecognizeData(galleryRecognize: DBGalleryRecognizeEntity)
}