package com.lighthouse.beep.model.gifticon

import com.lighthouse.beep.library.textformat.TextInputFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class GifticonDetail(
    val id: Long,
    val userId: String,
    val isCashCard: Boolean,
    val remainCash: Int,
    val totalCash: Int,
    val thumbnail: GifticonThumbnail,
    val name: String,
    val displayBrand: String,
    val barcodeType: GifticonBarcodeType,
    val barcode: String,
    val memo: String,
    val isUsed: Boolean,
    val expireAt: Date,
) {
    val displayBarcode = TextInputFormat.BARCODE.valueToTransformed(barcode)

    val isExpired: Boolean
        get() = Date() >= expireAt

    val formattedExpiredDate: String
        get() {
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return formatter.format(expireAt)
        }
}
