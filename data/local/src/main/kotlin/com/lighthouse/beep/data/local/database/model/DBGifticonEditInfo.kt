package com.lighthouse.beep.data.local.database.model

import android.graphics.Rect
import android.net.Uri
import androidx.room.ColumnInfo
import java.util.Date

internal data class DBGifticonEditInfo(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "thumbnail_type") val thumbnailType: String,
    @ColumnInfo(name = "thumbnail_built_in_code") val thumbnailBuiltInCode: String,
    @ColumnInfo(name = "thumbnail_uri") val thumbnailUri: Uri?,
    @ColumnInfo(name = "thumbnail_rect") val thumbnailRect: Rect,
    @ColumnInfo(name = "gifticon_uri") val gifticonUri: Uri?,
    @ColumnInfo(name = "image_path") val imagePath: String,
    @ColumnInfo(name = "image_added_date") val imageAddedDate: Date,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "brand") val brand: String,
    @ColumnInfo(name = "display_brand") val displayBrand: String,
    @ColumnInfo(name = "barcode_type") val barcodeType: String,
    @ColumnInfo(name = "barcode") val barcode: String,
    @ColumnInfo(name = "is_cash_card") val isCashCard: Boolean,
    @ColumnInfo(name = "total_cash") val totalCash: Int,
    @ColumnInfo(name = "remain_cash") val remainCash: Int,
    @ColumnInfo(name = "memo") val memo: String,
    @ColumnInfo(name = "expire_at") val expireAt: Date,
    @ColumnInfo(name = "updated_at") val updatedAt: Date,
)