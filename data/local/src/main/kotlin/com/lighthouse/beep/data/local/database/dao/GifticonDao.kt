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
import java.util.Date

@Dao
internal interface GifticonDao {

    @Query(
        "SELECT 1 FROM gifticon_table WHERE user_id = :userId AND is_used = :isUsed LIMIT 1"
    )
    fun isExistGifticon(
        userId: String,
        isUsed: Boolean
    ): Flow<Boolean>

    @Query(
        "SELECT COUNT(1) FROM gifticon_table WHERE user_id = :userId AND is_used = :isUsed"
    )
    fun getGifticonCount(
        userId: String,
        isUsed: Boolean,
    ): Flow<Int>

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
    fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): Flow<DBGifticonDetail?>

    @Query(
        "SELECT thumbnail_uri, " +
                "gifticon_uri " +
                "FROM gifticon_table " +
                "WHERE user_id = :userId AND id in (:gifticonIdList)"
    )
    suspend fun getGifticonResourceList(
        userId: String,
        gifticonIdList: List<Long>,
    ): List<DBGifticonResource>

    @Query(
        "SELECT thumbnail_uri, " +
                "gifticon_uri " +
                "FROM gifticon_table " +
                "WHERE user_id = :userId"
    )
    suspend fun getGifticonResourceList(
        userId: String,
    ): List<DBGifticonResource>

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
                "WHERE user_id = :userId AND is_used = :isUsed " +
                "ORDER BY " +
                "CASE WHEN :sortCode = 0 AND :isAsc = 0 THEN created_at END DESC, " +
                "CASE WHEN :sortCode = 0 AND :isAsc = 1 THEN created_at END ASC, " +
                "CASE WHEN :sortCode = 1 AND :isAsc = 0 THEN expire_at END DESC, " +
                "CASE WHEN :sortCode = 1 AND :isAsc = 1 THEN expire_at END ASC, " +
                "CASE WHEN :sortCode = 2 AND :isAsc = 0 THEN updated_at END DESC, " +
                "CASE WHEN :sortCode = 2 AND :isAsc = 1 THEN updated_at END ASC"
        ,
    )
    fun getGifticonList(
        userId: String,
        isUsed: Boolean,
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
                "CASE WHEN :sortCode = 1 AND :isAsc = 1 THEN expire_at END ASC, " +
                "CASE WHEN :sortCode = 2 AND :isAsc = 0 THEN updated_at END DESC, " +
                "CASE WHEN :sortCode = 2 AND :isAsc = 1 THEN updated_at END ASC"
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
    suspend fun insertGifticonList(list: List<DBGifticonEntity>): List<Long>

    @Update(entity = DBGifticonEntity::class)
    suspend fun updateGifticon(editInfo: DBGifticonEditInfo)

    @Query("DELETE FROM gifticon_table WHERE user_id = :userId")
    suspend fun deleteGifticon(userId: String): Int

    @Query("DELETE FROM gifticon_table WHERE user_id = :userId AND id in (:gifticonId)")
    suspend fun deleteGifticon(userId: String, gifticonId: List<Long>): Int

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
                "SET is_used = 1, remain_cash = 0, updated_at = :updated " +
                "WHERE user_id = :userId AND id in (:gifticonId)",
    )
    suspend fun useGifticonList(
        userId: String,
        gifticonId: List<Long>,
        updated: Date,
    )

    @Query(
        "UPDATE gifticon_table " +
                "SET is_used = 0, remain_cash = total_cash, updated_at = :updated " +
                "WHERE user_id = :userId AND id in (:gifticonId)",
    )
    suspend fun revertGifticonList(
        userId: String,
        gifticonId: List<Long>,
        updated: Date,
    )

    @Query(
        "UPDATE gifticon_table " +
                "SET is_used = :isUsed, remain_cash = :remain, updated_at = :updated " +
                "WHERE user_id = :userId AND id = :gifticonId",
    )
    suspend fun updateGifticonUseInfo(
        userId: String,
        gifticonId: Long,
        isUsed: Boolean,
        remain: Int,
        updated: Date,
    )
}