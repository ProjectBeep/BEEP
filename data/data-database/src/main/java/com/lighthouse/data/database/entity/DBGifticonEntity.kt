package com.lighthouse.data.database.entity

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lighthouse.data.database.entity.DBGifticonEntity.Companion.GIFTICON_TABLE
import java.util.Date

@Entity(tableName = GIFTICON_TABLE)
internal data class DBGifticonEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "created_at") val createdAt: Date,
    @ColumnInfo(name = "user_id") val userId: String,
    @ColumnInfo(name = "has_image") val hasImage: Boolean,
    @ColumnInfo(name = "cropped_uri") val croppedUri: Uri?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "expire_at") val expireAt: Date,
    @ColumnInfo(name = "barcode") val barcode: String,
    @ColumnInfo(name = "is_cash_card") val isCashCard: Boolean,
    @ColumnInfo(name = "total_cash") val totalCash: Int,
    @ColumnInfo(name = "remain_cash") val remainCash: Int,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "is_used") val isUsed: Boolean
) {
    companion object {
        const val GIFTICON_TABLE = "gifticon_table"
    }
}
