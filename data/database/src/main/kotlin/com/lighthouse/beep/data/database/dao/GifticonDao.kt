package com.lighthouse.beep.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lighthouse.beep.data.database.entity.DBGifticonEntity
import com.lighthouse.beep.data.database.entity.DBUsageHistoryEntity
import com.lighthouse.beep.data.database.model.DBBrandCategory
import com.lighthouse.beep.data.database.model.DBGifticonDetail
import com.lighthouse.beep.data.database.model.DBGifticonListItem
import com.lighthouse.beep.data.database.model.DBGifticonNotification
import com.lighthouse.beep.model.exception.common.NotFoundException
import com.lighthouse.beep.model.exception.db.UpdateException
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
internal interface GifticonDao {

    /**
     * 기프티콘과 크롭정보를 합쳐서 가져오기
     * */
    @Query(
        "SELECT * FROM gifticon_table  " +
            "WHERE user_id = :userId AND id = :gifticonId " +
            "LIMIT 1",
    )
    suspend fun getGifticonDetail(
        userId: String,
        gifticonId: Long,
    ): DBGifticonDetail?

    /**
     * 기프티콘 정보를 추가한다
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGifticon(gifticon: DBGifticonEntity)

    @Update
    suspend fun updateGifticon(gifticon: DBGifticonEntity)

    /**
     * 기프티콘 정보 삭제한다
     * */
    @Query(
        "DELETE FROM gifticon_table WHERE user_id = :userId AND id = :gifticonId",
    )
    suspend fun deleteGifticon(userId: String, gifticonId: Long): Int

    /**
     * 특정 유저의 기프티콘 정보를 다른 유저에게 넘긴다
     * */
    @Query(
        "UPDATE gifticon_table " +
            "SET user_id = :newUserId " +
            "WHERE user_id = :oldUserId",
    )
    suspend fun transferGifticon(oldUserId: String, newUserId: String)

    /**
     * 브랜드 존재여부 확인
     * */
    @Query(
        "SELECT EXISTS(" +
            "SELECT 1 from gifticon_table " +
            "WHERE user_id = :userId AND brand=:brand)",
    )
    suspend fun hasGifticonBrand(
        userId: String,
        brand: String,
    ): Boolean

    /**
     * 기프티콘 정보를 가져온다
     * sortCode : SortBy 참고
     * 0 : Recent
     * 1 : Deadline
     * */
    @Query(
        "SELECT id, cropped_uri, name, display_brand, is_cash_card, total_cash, remain_cash, " +
            "Cast(" +
            "JulianDay(date(expire_at / 1000), 'unixepoch') - " +
            "JulianDay(date('now')) as Integer" +
            ") as d_day, is_used, expire_at, created_at " +
            "FROM gifticon_table " +
            "WHERE CASE " +
            "WHEN :excludeExpire = 1 THEN user_id = :userId AND (is_used = :isUsed OR updated_at > :updatedAt) AND d_day > 0 " +
            "ELSE user_id = :userId AND is_used = 0 " +
            "END " +
            "ORDER BY " +
            "CASE WHEN :sortCode = 0 AND :isAsc = 0 THEN created_at END DESC, " +
            "CASE WHEN :sortCode = 0 AND :isAsc = 1 THEN created_at END ASC, " +
            "CASE WHEN :sortCode = 1 AND :isAsc = 0 THEN expire_at END DESC, " +
            "CASE WHEN :sortCode = 1 AND :isAsc = 1 THEN expire_at END ASC " +
            "LIMIT :offset, :limit",
    )
    suspend fun getGifticonList(
        userId: String,
        isUsed: Boolean,
        updatedAt: Date,
        excludeExpire: Int,
        sortCode: Int,
        isAsc: Int,
        offset: Int,
        limit: Int,
    ): List<DBGifticonListItem>

    /**
     * 기프티콘 정보를 브랜드이름으로 가져온다
     * brand == "" : 전체 리스트
     * sortCode : SortBy 참고
     * 0 : Recent
     * 1 : Deadline
     * */
    @Query(
        "SELECT id, cropped_uri, name, display_brand, is_cash_card, total_cash, remain_cash, " +
            "Cast(" +
            "JulianDay(date(expire_at / 1000), 'unixepoch') - " +
            "JulianDay(date('now')) as Integer" +
            ") as d_day, is_used, expire_at, created_at " +
            "FROM gifticon_table " +
            "WHERE CASE " +
            "WHEN IFNULL(:filters, '') = '' THEN " +
            "CASE WHEN :excludeExpire = 1 THEN user_id = :userId AND (is_used = :isUsed OR updated_at > :updatedAt) AND d_day > 0 " +
            "ELSE user_id = :userId AND is_used = 0 " +
            "END " +
            "ELSE " +
            "CASE WHEN :excludeExpire = 1 THEN brand = (:filters) AND user_id = :userId AND (is_used = :isUsed OR updated_at > :updatedAt) AND d_day > 0 " +
            "ELSE brand IN (:filters) AND user_id = :userId AND is_used = 0 " +
            "END " +
            "END " +
            "ORDER BY " +
            "CASE WHEN :sortCode = 0 AND :isAsc = 0 THEN created_at END DESC, " +
            "CASE WHEN :sortCode = 0 AND :isAsc = 1 THEN created_at END ASC, " +
            "CASE WHEN :sortCode = 1 AND :isAsc = 0 THEN expire_at END DESC, " +
            "CASE WHEN :sortCode = 1 AND :isAsc = 1 THEN expire_at END ASC " +
            "LIMIT :offset, :limit",
    )
    suspend fun getGifticonByBrand(
        userId: String,
        filters: Set<String>?,
        isUsed: Boolean,
        updatedAt: Date,
        excludeExpire: Int,
        sortCode: Int,
        isAsc: Int,
        offset: Int,
        limit: Int,
    ): List<DBGifticonListItem>

    /**
     * Notification 에서 보여주기위해 DDay Set에 있는 Gifticon 정보를 가져옴
     */
    @Query(
        "SELECT id, name, " +
            "Cast(" +
            "JulianDay(date(expire_at / 1000), 'unixepoch') - " +
            "JulianDay(date('now')) as Integer" +
            ") as d_day " +
            "FROM gifticon_table " +
            "WHERE user_id = :userId AND is_used = 0 AND d_day in (:dDaySet) " +
            "ORDER BY expire_at",
    )
    suspend fun getGifticonNotifications(
        userId: String,
        dDaySet: Set<Int>,
    ): List<DBGifticonNotification>

    /**
     * 브랜드 이름과 해당 브랜드의 기프티콘 개수를 가져오기
     * 개수를 기준으로 정렬
     * */
    @Query(
        "SELECT display_brand, COUNT(*) AS count FROM gifticon_table " +
            "WHERE CASE " +
            "WHEN :isUsed = 1 THEN user_id = :userId AND is_used = :isUsed " +
            "WHEN :excludeExpire = 1 THEN user_id = :userId AND is_used = :isUsed AND expire_at > :now " +
            "ELSE user_id = :userId AND is_used = :isUsed " +
            "END " +
            "GROUP BY brand ORDER BY count DESC",
    )
    fun getBrandCategoryList(
        userId: String,
        isUsed: Int,
        excludeExpire: Int,
        now: Date,
    ): Flow<List<DBBrandCategory>>

    /**
     * 기프티콘의 잔액을 반환한다
     * */
    @Query(
        "SELECT remain_cash FROM gifticon_table " +
            "WHERE user_id = :userId AND id = :gifticonId",
    )
    suspend fun getCurrentGifticonRemainCash(
        userId: String,
        gifticonId: Long,
    ): Int

    /**
     * 현재 금액과 사용기록을 합쳐서 총 금액을 반환한다
     * */
    @Query(
        "SELECT SUM(remain_cash + " +
            "(SELECT SUM(amount) FROM usage_history_table " +
            "WHERE gifticon_id = :gifticonId " +
            "GROUP BY gifticon_id)) " +
            "FROM gifticon_table " +
            "WHERE user_id = :userId AND id = :gifticonId " +
            "LIMIT 1",
    )
    suspend fun getTotalGifticonRemainCash(
        userId: String,
        gifticonId: Long,
    ): Int

    /**
     * 기프티콘이 금액권인지 확인한다
     * */
    @Query(
        "SELECT is_cash_card FROM gifticon_table " +
            "WHERE user_id = :userId AND id = :gifticonId",
    )
    suspend fun getGifticonIsCashCard(
        userId: String,
        gifticonId: Long,
    ): Boolean

    /**
     * 기프티콘을 사용 상태를 변경한다
     * */
    @Query(
        "UPDATE gifticon_table " +
            "SET is_used = :isUsed " +
            "WHERE user_id = :userId AND id = :gifticonId",
    )
    suspend fun updateGifticonUsed(
        userId: String,
        gifticonId: Long,
        isUsed: Boolean,
    ): Int

    /**
     * 금액권 기프티콘의 잔액을 변경한다
     * */
    @Query(
        "UPDATE gifticon_table " +
            "SET remain_cash = :remainCash " +
            "WHERE user_id = :userId AND id = :gifticonId",
    )
    suspend fun updateGifticonRemainCash(
        userId: String,
        gifticonId: Long,
        remainCash: Int,
    ): Int

    /**
     * 기프티콘의 사용 기록을 추가한다
     * */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUsageHistory(usageHistory: DBUsageHistoryEntity)

    /**
     * 기프티콘을 사용, 사용기록 추가
     *
     * DBNotFoundException
     * 업데이트 된 정보가 없을 경우 에러
     * */
    @Transaction
    suspend fun useGifticonAndInsertHistory(
        userId: String,
        usageHistory: DBUsageHistoryEntity,
    ) {
        val gifticonId = usageHistory.gifticonId

        if (updateGifticonUsed(userId, gifticonId, true) == 0) {
            throw NotFoundException("업데이트할 기프티콘을 찾을 수 없습니다.")
        }

        insertUsageHistory(usageHistory)
    }

    /**
     * 금액권 기프티콘을 사용, 사용기록 추가
     *
     * DBUpdateException
     * 사용할 금액이 잔액보다 많은 경우 에러
     *
     * DBNotFoundException
     * 업데이트 된 정보가 없을 경우 에러
     * */
    @Transaction
    suspend fun useCashCardGifticonAndInsertHistory(
        userId: String,
        amount: Int,
        usageHistory: DBUsageHistoryEntity,
    ) {
        val gifticonId = usageHistory.gifticonId
        val balance = getCurrentGifticonRemainCash(userId, gifticonId)

        if (balance < amount) {
            throw UpdateException("사용할 금액이 잔액보다 많습니다.")
        }

        if (updateGifticonRemainCash(userId, gifticonId, balance - amount) == 0) {
            throw NotFoundException("업데이트할 기프티콘을 찾을 수 없습니다.")
        }

        insertUsageHistory(usageHistory)

        if (balance == amount) {
            if (updateGifticonUsed(userId, gifticonId, true) == 0) {
                throw NotFoundException("업데이트할 기프티콘을 찾을 수 없습니다.")
            }
        }
    }

    /**
     * 기프티콘 사용을 되돌립니다.
     *
     * DBNotFoundException
     * 업데이트 된 정보가 없을 경우 에러
     * */
    @Transaction
    suspend fun revertUsedGifticonAndDeleteHistory(
        userId: String,
        gifticonId: Long,
    ) {
        val isCashCard = getGifticonIsCashCard(userId, gifticonId)
        if (isCashCard) {
            val totalBalance = getTotalGifticonRemainCash(userId, gifticonId)
            if (updateGifticonRemainCash(userId, gifticonId, totalBalance) == 0) {
                throw NotFoundException("기프티콘의 정보를 찾을 수 없습니다.")
            }
        }
        if (updateGifticonUsed(userId, gifticonId, false) == 0) {
            throw NotFoundException("기프티콘의 정보를 찾을 수 없습니다.")
        }
        deleteUsageHistory(userId, gifticonId)
    }

    /**
     * 기프티콘의 사용 기록을 조회한다
     * */
    @Query(
        "SELECT * FROM usage_history_table " +
            "WHERE gifticon_id = :gifticonId AND " +
            "EXISTS(" +
            "SELECT 1 FROM gifticon_table AS gt " +
            "WHERE gt.user_id = :userId AND gt.id = :gifticonId)",
    )
    fun getUsageHistory(
        userId: String,
        gifticonId: Long,
    ): Flow<List<DBUsageHistoryEntity>>

    /**
     * 기프티콘을 삭제한다
     * */
    @Query(
        "DELETE FROM usage_history_table " +
            "WHERE gifticon_id = :gifticonId AND " +
            "EXISTS(" +
            "SELECT 1 FROM gifticon_table AS gt " +
            "WHERE gt.user_id = :userId AND gt.id = :gifticonId)",
    )
    fun deleteUsageHistory(
        userId: String,
        gifticonId: Long,
    )
}
