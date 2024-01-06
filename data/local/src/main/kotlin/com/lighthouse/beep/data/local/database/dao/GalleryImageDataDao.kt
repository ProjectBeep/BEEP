package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lighthouse.beep.data.local.database.entity.DBGalleryImageDataEntity
import java.util.Date

@Dao
internal interface GalleryImageDataDao {

    @Query("SELECT * FROM gallery_image_data")
    suspend fun getGalleryImageDataList(): List<DBGalleryImageDataEntity>

    @Insert(onConflict=OnConflictStrategy.REPLACE)
    suspend fun insertGalleryImageData(galleryRecognize: DBGalleryImageDataEntity)

    @Query(
        "UPDATE gallery_image_data " +
                "SET added_gifticon_id = :gifticonId " +
                "WHERE image_path = :imagePath AND added_data = :addedData"
    )
    suspend fun updateAddedGifticonId(
        gifticonId: Long?,
        imagePath: String,
        addedData: Date,
    )

    @Query(
        "UPDATE gallery_image_data " +
                "SET added_gifticon_id = null " +
                "WHERE added_gifticon_id in (:gifticonIdList)"
    )
    suspend fun deleteAddedGifticonId(
        gifticonIdList: List<Long>,
    )
}