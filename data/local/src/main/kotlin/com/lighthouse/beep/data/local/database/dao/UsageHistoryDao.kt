package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.lighthouse.beep.data.local.database.entity.DBUsageHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UsageHistoryDao {

    @Insert
    suspend fun insertUsageHistory(history: DBUsageHistoryEntity): Long

    @Update
    suspend fun updateUsageHistory(history: DBUsageHistoryEntity)

    @Delete
    suspend fun deleteUsageHistory(history: DBUsageHistoryEntity)

    @Query("SELECT * FROM usage_history_table WHERE item_id = :itemId ORDER BY used_at DESC")
    fun getUsageHistoryByItem(itemId: Long): Flow<List<DBUsageHistoryEntity>>

    @Query("SELECT * FROM usage_history_table WHERE used_by = :userId ORDER BY used_at DESC")
    fun getUsageHistoryByUser(userId: String): Flow<List<DBUsageHistoryEntity>>

    @Query("SELECT * FROM usage_history_table WHERE group_id = :groupId ORDER BY used_at DESC")
    fun getUsageHistoryByGroup(groupId: String): Flow<List<DBUsageHistoryEntity>>

    @Query("SELECT * FROM usage_history_table ORDER BY used_at DESC LIMIT :limit")
    fun getRecentUsageHistory(limit: Int): Flow<List<DBUsageHistoryEntity>>

    @Query("SELECT * FROM usage_history_table WHERE firebase_id IS NULL")
    suspend fun getUnSyncedUsageHistory(): List<DBUsageHistoryEntity>

    @Query("UPDATE usage_history_table SET firebase_id = :firebaseId WHERE id = :id")
    suspend fun updateFirebaseId(id: Long, firebaseId: String)
}