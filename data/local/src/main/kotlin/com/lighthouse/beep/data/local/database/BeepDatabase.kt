package com.lighthouse.beep.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lighthouse.beep.data.local.database.converter.DateConverter
import com.lighthouse.beep.data.local.database.converter.DmsConverter
import com.lighthouse.beep.data.local.database.converter.RectConverter
import com.lighthouse.beep.data.local.database.converter.UriConverter
import com.lighthouse.beep.data.local.database.dao.DetailDao
import com.lighthouse.beep.data.local.database.dao.GroupDao
import com.lighthouse.beep.data.local.database.dao.ItemDao
import com.lighthouse.beep.data.local.database.dao.UsageHistoryDao
import com.lighthouse.beep.data.local.database.dao.UserDao
import com.lighthouse.beep.data.local.database.entity.DBGifticonDetailEntity
import com.lighthouse.beep.data.local.database.entity.DBGroupEntity
import com.lighthouse.beep.data.local.database.entity.DBGroupItemEntity
import com.lighthouse.beep.data.local.database.entity.DBItemEntity
import com.lighthouse.beep.data.local.database.entity.DBItemImageEntity
import com.lighthouse.beep.data.local.database.entity.DBMembershipDetailEntity
import com.lighthouse.beep.data.local.database.entity.DBPendingChangeEntity
import com.lighthouse.beep.data.local.database.entity.DBUsageHistoryEntity
import com.lighthouse.beep.data.local.database.entity.DBUserEntity
import com.lighthouse.beep.data.local.database.entity.DBWifiDetailEntity

@Database(
    entities = [
        DBItemEntity::class,
        DBUserEntity::class,
        DBGifticonDetailEntity::class,
        DBWifiDetailEntity::class,
        DBMembershipDetailEntity::class,
        DBItemImageEntity::class,
        DBGroupEntity::class,
        DBGroupItemEntity::class,
        DBUsageHistoryEntity::class,
        DBPendingChangeEntity::class,
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

    abstract fun itemDao(): ItemDao
    abstract fun userDao(): UserDao
    abstract fun groupDao(): GroupDao
    abstract fun detailDao(): DetailDao
    abstract fun usageHistoryDao(): UsageHistoryDao

    companion object {
        const val DATABASE_NAME = "beep_database"
    }
}
