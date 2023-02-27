package com.lighthouse.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lighthouse.data.database.entity.DBUsageHistoryEntity
import com.lighthouse.data.database.exception.DBNotFoundException
import com.lighthouse.data.database.exception.DBUpdateException
import kotlinx.coroutines.flow.Flow

@Dao
internal interface GifticonUsageHistoryDao {

    /**
     * 기프티콘이 금액권인지 확인한다
     * 1. 유저 ID
     * 2. 기프티콘 ID
     * */
    @Query(
        "SELECT is_cash_card FROM gifticon_table " +
            "WHERE user_id = :userId AND id = :gifticonId"
    )
    suspend fun getGifticonIsCashCard(
        userId: String,
        gifticonId: String
    ): Boolean

    /**
     * 기프티콘의 잔액을 반환한다
     * 1. 유저 ID
     * 2. 기프티콘 ID
     * */
    @Query(
        "SELECT remain_cash FROM gifticon_table " +
            "WHERE user_id = :userId AND id = :gifticonId"
    )
    suspend fun getCurrentGifticonRemainCash(
        userId: String,
        gifticonId: String
    ): Int

    /**
     * 현재 금액과 사용기록을 합쳐서 총 금액을 반환한다
     * 1. 유저 ID
     * 2. 기프티콘 ID
     * */
    @Query(
        "SELECT SUM(remain_cash + " +
            "(SELECT SUM(amount) FROM usage_history_table " +
            "WHERE gifticon_id = :gifticonId " +
            "GROUP BY gifticon_id)) " +
            "FROM gifticon_table " +
            "WHERE user_id = :userId AND id = :gifticonId " +
            "LIMIT 1"
    )
    suspend fun getTotalGifticonRemainCash(
        userId: String,
        gifticonId: String
    ): Int

    /**
     * 기프티콘을 사용 상태를 변경한다
     * 1. 유저 ID
     * 2. 기프티콘 ID
     * 3. 사용여부
     * */
    @Query(
        "UPDATE gifticon_table " +
            "SET is_used = :isUsed " +
            "WHERE user_id = :userId AND id = :gifticonId"
    )
    suspend fun updateGifticonUsed(
        userId: String,
        gifticonId: String,
        isUsed: Boolean
    ): Int

    /**
     * 금액권 기프티콘의 잔액을 변경한다
     * 1. 유저 ID
     * 2. 기프티콘 ID
     * 3. 잔액
     * */
    @Query(
        "UPDATE gifticon_table " +
            "SET remain_cash = :remainCash " +
            "WHERE user_id = :userId AND id = :gifticonId"
    )
    suspend fun updateGifticonRemainCash(
        userId: String,
        gifticonId: String,
        remainCash: Int
    ): Int

    /**
     * 기프티콘의 사용 기록을 추가한다
     * 1. 사용 기록
     * */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUsageHistory(usageHistory: DBUsageHistoryEntity)

    /**
     * 기프티콘을 사용, 사용기록 추가
     * 1. 유저 ID
     * 2. 사용기록
     * DBNotFoundException
     * 업데이트 된 정보가 없을 경우 에러
     * */
    @Transaction
    suspend fun useGifticonAndInsertHistory(
        userId: String,
        usageHistory: DBUsageHistoryEntity
    ) {
        val gifticonId = usageHistory.gifticonId

        if (updateGifticonUsed(userId, gifticonId, true) == 0) {
            throw DBNotFoundException("업데이트할 기프티콘을 찾을 수 없습니다.")
        }

        insertUsageHistory(usageHistory)
    }

    /**
     * 금액권 기프티콘을 사용, 사용기록 추가
     * 1. 유저 ID
     * 2. 사용할 금액
     * 3. 사용기록
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
        usageHistory: DBUsageHistoryEntity
    ) {
        val gifticonId = usageHistory.gifticonId
        val balance = getCurrentGifticonRemainCash(userId, gifticonId)

        if (balance < amount) {
            throw DBUpdateException("사용할 금액이 잔액보다 많습니다.")
        }

        if (updateGifticonRemainCash(userId, gifticonId, balance - amount) == 0) {
            throw DBNotFoundException("업데이트할 기프티콘을 찾을 수 없습니다.")
        }

        insertUsageHistory(usageHistory)

        if (balance == amount) {
            if (updateGifticonUsed(userId, gifticonId, true) == 0) {
                throw DBNotFoundException("업데이트할 기프티콘을 찾을 수 없습니다.")
            }
        }
    }

    /**
     * 기프티콘 사용을 되돌립니다.
     * 1. 유저 ID
     * 2. 기프티콘 ID
     * DBNotFoundException
     * 업데이트 된 정보가 없을 경우 에러
     * */
    @Transaction
    suspend fun revertUsedGifticonAndDeleteHistory(
        userId: String,
        gifticonId: String
    ) {
        val isCashCard = getGifticonIsCashCard(userId, gifticonId)
        if (isCashCard) {
            val totalBalance = getTotalGifticonRemainCash(userId, gifticonId)
            if (updateGifticonRemainCash(userId, gifticonId, totalBalance) == 0) {
                throw DBNotFoundException("기프티콘의 정보를 찾을 수 없습니다.")
            }
        }
        if (updateGifticonUsed(userId, gifticonId, false) == 0) {
            throw DBNotFoundException("기프티콘의 정보를 찾을 수 없습니다.")
        }
        deleteUsageHistory(userId, gifticonId)
    }

    /**
     * 기프티콘의 사용 기록을 조회한다
     * 1. 유저 ID
     * 2. 기프티콘 ID
     * */
    @Query(
        "SELECT * FROM usage_history_table " +
            "WHERE gifticon_id = :gifticonId AND " +
            "EXISTS(" +
            "SELECT 1 FROM gifticon_table AS gt " +
            "WHERE gt.user_id = :userId AND gt.id = :gifticonId" +
            ")"
    )
    fun getUsageHistory(
        userId: String,
        gifticonId: String
    ): Flow<List<DBUsageHistoryEntity>>

    /**
     * 기프티콘을 삭제한다
     * 1. 유저 ID
     * 2. 기프티콘 ID
     * */
    @Query(
        "DELETE FROM usage_history_table " +
            "WHERE gifticon_id = :gifticonId AND " +
            "EXISTS(" +
            "SELECT 1 FROM gifticon_table AS gt " +
            "WHERE gt.user_id = :userId AND gt.id = :gifticonId" +
            ")"
    )
    fun deleteUsageHistory(
        userId: String,
        gifticonId: String
    )
}
