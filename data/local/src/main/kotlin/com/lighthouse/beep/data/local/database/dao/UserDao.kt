package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lighthouse.beep.data.local.database.entity.DBUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: DBUserEntity)

    @Update
    suspend fun updateUser(user: DBUserEntity)

    @Query("SELECT * FROM users_table WHERE id = :userId")
    suspend fun getUserById(userId: String): DBUserEntity?

    @Query("SELECT * FROM users_table WHERE id = :userId")
    fun getUserByIdFlow(userId: String): Flow<DBUserEntity?>

    @Query("SELECT * FROM users_table")
    suspend fun getAllUsers(): List<DBUserEntity>

    @Query("DELETE FROM users_table WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}