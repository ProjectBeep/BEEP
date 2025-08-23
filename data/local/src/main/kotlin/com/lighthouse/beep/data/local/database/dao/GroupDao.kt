package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lighthouse.beep.data.local.database.entity.DBGroupEntity
import com.lighthouse.beep.data.local.database.entity.DBGroupItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: DBGroupEntity)

    @Update
    suspend fun updateGroup(group: DBGroupEntity)

    @Delete
    suspend fun deleteGroup(group: DBGroupEntity)

    @Query("SELECT * FROM groups_table WHERE id = :groupId")
    suspend fun getGroupById(groupId: String): DBGroupEntity?

    @Query("SELECT * FROM groups_table ORDER BY joined_at DESC")
    fun getAllGroups(): Flow<List<DBGroupEntity>>

    @Query("SELECT * FROM groups_table WHERE invite_code = :inviteCode")
    suspend fun getGroupByInviteCode(inviteCode: String): DBGroupEntity?

    // 그룹-아이템 연결 관리
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroupItem(groupItem: DBGroupItemEntity)

    @Query("DELETE FROM group_items_table WHERE group_id = :groupId AND item_id = :itemId")
    suspend fun removeGroupItem(groupId: String, itemId: Long)

    @Query("SELECT * FROM group_items_table WHERE group_id = :groupId")
    suspend fun getGroupItems(groupId: String): List<DBGroupItemEntity>

    @Query("SELECT * FROM group_items_table WHERE item_id = :itemId")
    suspend fun getItemGroups(itemId: Long): List<DBGroupItemEntity>

    @Transaction
    @Query("""
        SELECT i.* FROM items_table i
        INNER JOIN group_items_table gi ON i.id = gi.item_id
        WHERE gi.group_id = :groupId
        ORDER BY gi.shared_at DESC
    """)
    fun getItemsInGroup(groupId: String): Flow<List<com.lighthouse.beep.data.local.database.entity.DBItemEntity>>
}