package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lighthouse.beep.data.local.database.entity.DBItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ItemDao {

    @Insert
    suspend fun insertItem(item: DBItemEntity): Long

    @Update
    suspend fun updateItem(item: DBItemEntity)

    @Delete
    suspend fun deleteItem(item: DBItemEntity)

    @Query("SELECT * FROM items_table WHERE id = :itemId")
    suspend fun getItemById(itemId: Long): DBItemEntity?

    @Query("SELECT * FROM items_table WHERE type = :type")
    suspend fun getItemsByType(type: String): List<DBItemEntity>

    @Query("SELECT * FROM items_table WHERE owner_id = :ownerId")
    fun getItemsByOwner(ownerId: String): Flow<List<DBItemEntity>>

    @Query("SELECT * FROM items_table ORDER BY created_at DESC")
    fun getAllItems(): Flow<List<DBItemEntity>>

    @Query("SELECT * FROM items_table WHERE sync_status != 'SYNCED'")
    suspend fun getUnSyncedItems(): List<DBItemEntity>

    @Query("UPDATE items_table SET sync_status = :status WHERE id = :itemId")
    suspend fun updateSyncStatus(itemId: Long, status: String)

    @Query("SELECT * FROM items_table WHERE firebase_id = :firebaseId")
    suspend fun getItemByFirebaseId(firebaseId: String): DBItemEntity?
}