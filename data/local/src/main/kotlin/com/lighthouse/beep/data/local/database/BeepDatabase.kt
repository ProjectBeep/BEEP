package com.lighthouse.beep.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lighthouse.beep.data.local.database.converter.DateConverter
import com.lighthouse.beep.data.local.database.converter.DmsConverter
import com.lighthouse.beep.data.local.database.converter.RectConverter
import com.lighthouse.beep.data.local.database.converter.UriConverter
import com.lighthouse.beep.data.local.database.dao.BrandLocationDao
import com.lighthouse.beep.data.local.database.dao.GalleryImageDataDao
import com.lighthouse.beep.data.local.database.dao.GifticonDao
import com.lighthouse.beep.data.local.database.entity.DBBrandLocationEntity
import com.lighthouse.beep.data.local.database.entity.DBBrandSectionEntity
import com.lighthouse.beep.data.local.database.entity.DBGalleryImageDataEntity
import com.lighthouse.beep.data.local.database.entity.DBGifticonEntity

@Database(
    entities = [
        DBGifticonEntity::class,
        DBBrandSectionEntity::class,
        DBBrandLocationEntity::class,
        DBGalleryImageDataEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(
    DateConverter::class,
    DmsConverter::class,
    RectConverter::class,
    UriConverter::class,
)
internal abstract class BeepDatabase : RoomDatabase() {

    abstract fun gifticonDao(): GifticonDao

    abstract fun brandLocationDao(): BrandLocationDao

    abstract fun galleryImageDataDao(): GalleryImageDataDao

    companion object {
        const val DATABASE_NAME = "beep_database"
    }
}
