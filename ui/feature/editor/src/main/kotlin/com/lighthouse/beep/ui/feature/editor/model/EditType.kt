package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.Bitmap
import android.graphics.RectF
import androidx.annotation.StringRes
import com.lighthouse.beep.core.common.exts.EMPTY_RECT_F
import com.lighthouse.beep.library.recognizer.BalanceRecognizer
import com.lighthouse.beep.library.recognizer.BarcodeRecognizer
import com.lighthouse.beep.library.recognizer.ExpiredRecognizer
import com.lighthouse.beep.library.recognizer.TextRecognizer
import com.lighthouse.beep.ui.dialog.textinput.TextInputFormat
import com.lighthouse.beep.ui.dialog.textinput.TextInputParam
import com.lighthouse.beep.ui.feature.editor.R

@Suppress("unused")
internal enum class EditType(@StringRes val textResId: Int) {
    MEMO(R.string.editor_gifticon_property_memo) {
        override val maxLength: Int = 20

        override fun createEditDataWithText(value: String): EditData {
            return EditData.Memo(value)
        }

        override fun createTextInputParam(data: GifticonData): TextInputParam {
            return TextInputParam(
                text = data.memo,
                maxLength = maxLength,
                inputFormat = TextInputFormat.TEXT,
            )
        }

        override fun getText(data: GifticonData): String {
            return data.memo
        }
    },
    THUMBNAIL(R.string.editor_gifticon_property_thumbnail) {
        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: RectF, zoom: Float): EditData {
            return EditData.Thumbnail(bitmap, rect, zoom)
        }

        override fun getCropData(data: GifticonData?): GifticonCropData {
            return data?.thumbnailCropData ?: GifticonCropData.None
        }
    },
    NAME(R.string.editor_gifticon_property_name) {
        private val textRecognizer = TextRecognizer()

        override fun createEditDataWithText(value: String): EditData {
            return EditData.Name(value)
        }

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: RectF, zoom: Float): EditData {
            val value = textRecognizer.recognize(bitmap).joinToString("")
            return EditData.CropName(value, rect, zoom)
        }

        override fun createTextInputParam(data: GifticonData): TextInputParam {
            return TextInputParam(
                text = data.name,
                inputFormat = TextInputFormat.TEXT,
            )
        }

        override fun isInvalid(data: GifticonData): Boolean {
            return data.name.isEmpty()
        }

        override fun getText(data: GifticonData): String {
            return data.name
        }

        override fun getCropData(data: GifticonData?): GifticonCropData {
            return data?.nameCropData ?: GifticonCropData.None
        }
    },
    BRAND(R.string.editor_gifticon_property_brand) {
        private val textRecognizer = TextRecognizer()

        override fun createEditDataWithText(value: String): EditData {
            return EditData.Brand(value)
        }

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: RectF, zoom: Float): EditData {
            val value = textRecognizer.recognize(bitmap).joinToString("")
            return EditData.CropBrand(value, rect, zoom)
        }

        override fun createTextInputParam(data: GifticonData): TextInputParam {
            return TextInputParam(
                text = data.brand,
                inputFormat = TextInputFormat.TEXT,
            )
        }

        override fun isInvalid(data: GifticonData): Boolean {
            return data.brand.isEmpty()
        }

        override fun getText(data: GifticonData): String {
            return data.brand
        }

        override fun getCropData(data: GifticonData?): GifticonCropData {
            return data?.brandCropData ?: GifticonCropData.None
        }
    },
    BARCODE(R.string.editor_gifticon_property_barcode) {
        private val barcodeRecognizer = BarcodeRecognizer()
        private val validBarcodeCount = setOf(12, 14, 16, 18, 20, 22, 24)

        override fun createEditDataWithText(value: String): EditData {
            return EditData.Barcode(value)
        }

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: RectF, zoom: Float): EditData {
            val value = barcodeRecognizer.recognize(bitmap)
            return EditData.CropBarcode(value, rect, zoom)
        }

        override fun createTextInputParam(data: GifticonData): TextInputParam {
            return TextInputParam(
                text = data.barcode,
                inputFormat = TextInputFormat.BARCODE,
            )
        }

        override fun isInvalid(data: GifticonData): Boolean {
            return data.barcode.length !in validBarcodeCount
        }

        override fun getText(data: GifticonData): String {
            return data.displayBarcode
        }

        override fun getCropData(data: GifticonData?): GifticonCropData {
            return data?.barcodeCropData ?: GifticonCropData.None
        }
    },
    EXPIRED(R.string.editor_gifticon_property_expired) {
        private val expiredRecognizer = ExpiredRecognizer()

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: RectF, zoom: Float): EditData {
            val value = expiredRecognizer.recognize(bitmap)
            return EditData.CropExpired(value, rect, zoom)
        }

        override fun getText(data: GifticonData): String {
            return data.displayExpired
        }

        override fun getCropData(data: GifticonData?): GifticonCropData {
            return data?.expiredCropData ?: GifticonCropData.None
        }
    },
    BALANCE(R.string.editor_gifticon_property_balance) {
        private val balanceRecognizer = BalanceRecognizer()

        override fun createEditDataWithText(value: String): EditData {
            return EditData.Balance(value)
        }

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: RectF, zoom: Float): EditData {
            val value = balanceRecognizer.recognize(bitmap).toString()
            return EditData.CropBalance(value, rect, zoom)
        }

        override fun createTextInputParam(data: GifticonData): TextInputParam {
            return TextInputParam(
                text = data.balance,
                inputFormat = TextInputFormat.BALANCE,
            )
        }

        override fun isInvalid(data: GifticonData): Boolean {
            return data.isCashCard && data.balance == "0"
        }

        override fun getText(data: GifticonData): String {
            return data.displayBalance
        }

        override fun getCropData(data: GifticonData?): GifticonCropData {
            return data?.balanceCropData ?: GifticonCropData.None
        }
    };

    open val maxLength = Int.MAX_VALUE

    open fun createEditDataWithText(value: String): EditData = EditData.None

    open suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: RectF, zoom: Float): EditData =
        EditData.None

    open fun createTextInputParam(data: GifticonData) = TextInputParam.None

    open fun isInvalid(data: GifticonData): Boolean = false

    open fun getText(data: GifticonData): String = ""

    open fun getCropData(data: GifticonData?): GifticonCropData = GifticonCropData.None
}