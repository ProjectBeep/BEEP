package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.RectF
import androidx.core.graphics.toRectF
import com.lighthouse.beep.core.common.exts.toFormattedString
import com.lighthouse.beep.model.gifticon.GifticonRecognizeResult
import com.lighthouse.beep.ui.dialog.textinput.TextInputFormat
import java.util.Date

internal data class GifticonData(
    val cropImageRect: RectF = RectF(),
    val name: String = "",
    val nameRect: RectF = RectF(),
    val brand: String = "",
    val brandRect: RectF = RectF(),
    val barcode: String = "",
    val barcodeRect: RectF = RectF(),
    val expired: Date = EMPTY_DATE,
    val expiredRect: RectF = RectF(),
    val isCashCard: Boolean = false,
    val balance: String = "",
    val balanceRect: RectF = RectF(),
    val memo: String = "",
) {
    companion object {
        private val EMPTY_DATE = Date(0)

        private val VALID_BARCODE_COUNT = setOf(12, 14, 16, 18, 20, 22, 24)
    }

    val displayBarcode: String = TextInputFormat.BARCODE.valueToTransformed(barcode)

    val displayExpired: String = if (expired == EMPTY_DATE) "" else expired.toFormattedString()

    val displayBalance: String = TextInputFormat.BALANCE.valueToTransformed(balance)

    val isInvalid
        get() = name.isEmpty() ||
                brand.isEmpty() ||
                barcode.length !in VALID_BARCODE_COUNT ||
                expired == EMPTY_DATE ||
                (isCashCard && balance.isEmpty())

    fun isInvalid(type: PropertyType): Boolean {
        return when(type) {
            PropertyType.NAME -> name.isEmpty()
            PropertyType.BRAND -> brand.isEmpty()
            PropertyType.BARCODE -> barcode.length !in VALID_BARCODE_COUNT
            PropertyType.EXPIRED -> expired == EMPTY_DATE
            else -> false
        }
    }

    fun getText(type: PropertyType): String {
        return when(type) {
            PropertyType.NAME -> name
            PropertyType.BRAND -> brand
            PropertyType.BARCODE -> displayBarcode
            PropertyType.EXPIRED -> displayExpired
            else -> ""
        }
    }
}

internal fun GifticonRecognizeResult?.toGifticonData(): GifticonData {
    this ?: return GifticonData()
    return GifticonData(
        cropImageRect = croppedRect?.toRectF() ?: RectF(),
        name = name,
        brand = brandName,
        barcode = barcode,
        expired = expiredAt,
        isCashCard = isCashCard,
        balance = balance.toString(),
    )
}