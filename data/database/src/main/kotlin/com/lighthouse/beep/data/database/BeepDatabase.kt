package com.lighthouse.beep.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lighthouse.beep.data.database.converter.DateConverter
import com.lighthouse.beep.data.database.converter.DmsConverter
import com.lighthouse.beep.data.database.converter.RectConverter
import com.lighthouse.beep.data.database.converter.UriConverter
import com.lighthouse.beep.data.database.dao.BrandLocationDao
import com.lighthouse.beep.data.database.dao.GifticonDao
import com.lighthouse.beep.data.database.entity.DBBrandLocationEntity
import com.lighthouse.beep.data.database.entity.DBBrandSectionEntity
import com.lighthouse.beep.data.database.entity.DBGifticonEntity
import com.lighthouse.beep.data.database.entity.DBUsageHistoryEntity

@Database(
    entities = [
        DBGifticonEntity::class,
        DBBrandSectionEntity::class,
        DBBrandLocationEntity::class,
        DBUsageHistoryEntity::class,
    ],
    version = 1,
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

    companion object {
        const val DATABASE_NAME = "beep_database"
    }
}
