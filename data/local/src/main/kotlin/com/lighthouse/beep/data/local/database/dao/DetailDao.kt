package com.lighthouse.beep.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.lighthouse.beep.data.local.database.entity.DBGifticonDetailEntity
import com.lighthouse.beep.data.local.database.entity.DBWifiDetailEntity
import com.lighthouse.beep.data.local.database.entity.DBMembershipDetailEntity

@Dao
internal interface DetailDao {

    // Gifticon Details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGifticonDetail(detail: DBGifticonDetailEntity)

    @Update
    suspend fun updateGifticonDetail(detail: DBGifticonDetailEntity)

    @Delete
    suspend fun deleteGifticonDetail(detail: DBGifticonDetailEntity)

    @Query("SELECT * FROM gifticon_details_table WHERE item_id = :itemId")
    suspend fun getGifticonDetail(itemId: Long): DBGifticonDetailEntity?

    // WiFi Details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWifiDetail(detail: DBWifiDetailEntity)

    @Update
    suspend fun updateWifiDetail(detail: DBWifiDetailEntity)

    @Delete
    suspend fun deleteWifiDetail(detail: DBWifiDetailEntity)

    @Query("SELECT * FROM wifi_details_table WHERE item_id = :itemId")
    suspend fun getWifiDetail(itemId: Long): DBWifiDetailEntity?

    // Membership Details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMembershipDetail(detail: DBMembershipDetailEntity)

    @Update
    suspend fun updateMembershipDetail(detail: DBMembershipDetailEntity)

    @Delete
    suspend fun deleteMembershipDetail(detail: DBMembershipDetailEntity)

    @Query("SELECT * FROM membership_details_table WHERE item_id = :itemId")
    suspend fun getMembershipDetail(itemId: Long): DBMembershipDetailEntity?
}