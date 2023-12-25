package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lighthouse.beep.data.local.database.entity.DBGifticonEntity
import com.lighthouse.beep.data.local.database.model.DBBrandCategory
import com.lighthouse.beep.data.local.database.model.DBGifticonDetail
import com.lighthouse.beep.data.local.database.model.DBGifticonEditInfo
import com.lighthouse.beep.data.local.database.model.DBGifticonListItem
import com.lighthouse.beep.data.local.database.model.DBGifticonResource
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GifticonDao {

    @Query(
        "SELECT EXISTS (" +
                "SELECT 1 from gifticon_table " +
                "WHERE user_id = :userId AND is_used = :isUsed)"
    )
    fun isExistGifticon(
        userId: String,
        isUsed: Boolean
    ): Flow<Boolean>

    @Query(
        "SELECT id, " +
                "user_id, " +
                "thumbnail_type, " +
                "thumbnail_built_in_code, " +
                "thumbnail_uri, " +
                "thumbnail_rect, " +
                "name, " +
                "display_brand, " +
                "barcode, " +
                "is_cash_card, " +
                "total_cash, " +
                "remain_cash, " +
                "memo, " +
                "is_used, " +
                "expire_at " +
                "FROM gifticon_table " +
                "WHERE user_id = :userId AND id = :gifticonId " +
                "LIMIT 1",
    )
    suspend fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): DBGifticonDetail?

    @Query(
        "SELECT thumbnail_uri, " +
                "gifticon_uri " +
                "FROM gifticon_table " +
                "WHERE user_id = :userId AND id = :gifticonId " +
                "LIMIT 1"
    )
    suspend fun getGifticonResource(
        userId: String,
        gifticonId: Long,
    ): DBGifticonResource?

    @Query(
        "SELECT thumbnail_uri, " +
                "gifticon_uri " +
                "FROM gifticon_table " +
                "WHERE user_id = :userId"
    )
    suspend fun getGifticonResourceList(
        userId: String,
    ): List<DBGifticonResource>?

    @Query(
        "SELECT id, " +
                "user_id, " +
                "thumbnail_type, " +
                "thumbnail_built_in_code, " +
                "thumbnail_uri, " +
                "thumbnail_rect, " +
                "name, " +
                "display_brand, " +
                "is_cash_card, " +
                "total_cash, " +
                "remain_cash, " +
                "is_used, " +
                "expire_at " +
                "FROM gifticon_table " +
                "WHERE user_id = :userId AND is_used = 0 " +
                "ORDER BY " +
                "CASE WHEN :sortCode = 0 AND :isAsc = 0 THEN created_at END DESC, " +
                "CASE WHEN :sortCode = 0 AND :isAsc = 1 THEN created_at END ASC, " +
                "CASE WHEN :sortCode = 1 AND :isAsc = 0 THEN expire_at END DESC, " +
                "CASE WHEN :sortCode = 1 AND :isAsc = 1 THEN expire_at END ASC",
    )
    fun getGifticonList(
        userId: String,
        sortCode: Int,
        isAsc: Int,
    ): Flow<List<DBGifticonListItem>>

    @Query(
        "SELECT id, " +
                "user_id, " +
                "thumbnail_type, " +
                "thumbnail_built_in_code, " +
                "thumbnail_uri, " +
                "thumbnail_rect, " +
                "name, " +
                "display_brand, " +
                "is_cash_card, " +
                "total_cash, " +
                "remain_cash, " +
                "is_used, " +
                "expire_at " +
                "FROM gifticon_table " +
                "WHERE user_id = :userId AND is_used = 0 AND brand = :brand " +
                "ORDER BY " +
                "CASE WHEN :sortCode = 0 AND :isAsc = 0 THEN created_at END DESC, " +
                "CASE WHEN :sortCode = 0 AND :isAsc = 1 THEN created_at END ASC, " +
                "CASE WHEN :sortCode = 1 AND :isAsc = 0 THEN expire_at END DESC, " +
                "CASE WHEN :sortCode = 1 AND :isAsc = 1 THEN expire_at END ASC",
    )
    fun getGifticonListByBrand(
        userId: String,
        brand: String,
        sortCode: Int,
        isAsc: Int,
    ): Flow<List<DBGifticonListItem>>

    @Query(
        "SELECT id, " +
                "user_id, " +
                "thumbnail_type, " +
                "thumbnail_built_in_code, " +
                "thumbnail_uri, " +
                "thumbnail_rect, " +
                "name, " +
                "display_brand, " +
                "is_cash_card, " +
                "total_cash, " +
                "remain_cash, " +
                "is_used, " +
                "expire_at " +
                "FROM gifticon_table " +
                "WHERE user_id = :userId AND is_used = 1 " +
                "ORDER BY used_at DESC"
    )
    fun getUsedGifticonList(userId: String): Flow<List<DBGifticonListItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGifticon(list: DBGifticonEntity)

    @Update(entity = DBGifticonEntity::class)
    suspend fun updateGifticon(editInfo: DBGifticonEditInfo)

    @Query("DELETE FROM gifticon_table WHERE user_id = :userId")
    suspend fun deleteGifticon(userId: String): Int

    @Query("DELETE FROM gifticon_table WHERE user_id = :userId AND id = :gifticonId")
    suspend fun deleteGifticon(userId: String, gifticonId: Long): Int

    @Query(
        "UPDATE gifticon_table " +
            "SET user_id = :newUserId " +
            "WHERE user_id = :oldUserId",
    )
    suspend fun transferGifticon(oldUserId: String, newUserId: String)

    @Query(
        "SELECT display_brand, COUNT(*) AS count FROM gifticon_table " +
                "WHERE user_id = :userId AND is_used = 0 " +
                "GROUP BY brand ORDER BY count DESC"
    )
    fun getBrandCategoryList(userId: String): Flow<List<DBBrandCategory>>

    @Query(
        "UPDATE gifticon_table " +
                "SET is_used = 1 AND remain_cash = 0 " +
                "WHERE user_id = :userId AND id = :gifticonId",
    )
    suspend fun useGifticon(
        userId: String,
        gifticonId: Long,
    )

    @Query(
        "UPDATE gifticon_table " +
                "SET is_used = 1 AND remain_cash = :remainCash " +
                "WHERE user_id = :userId AND id = :gifticonId",
    )
    suspend fun useCashGifticon(
        userId: String,
        gifticonId: Long,
        remainCash: Int,
    ): Int

    @Query(
        "UPDATE gifticon_table " +
                "SET is_used = 1 AND total_cash = remain_cash " +
                "WHERE user_id = :userId AND id = :gifticonId",
    )
    suspend fun revertUsedGifticon(
        userId: String,
        gifticonId: Long,
    ): Int
}