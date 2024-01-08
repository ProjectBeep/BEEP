package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.Bitmap
import android.graphics.Rect
import androidx.annotation.StringRes
import com.google.mlkit.vision.barcode.common.Barcode
import com.lighthouse.beep.library.recognizer.BalanceRecognizer
import com.lighthouse.beep.library.recognizer.BarcodeRecognizer
import com.lighthouse.beep.library.recognizer.ExpiredRecognizer
import com.lighthouse.beep.library.recognizer.TextRecognizer
import com.lighthouse.beep.library.textformat.TextInputFormat
import com.lighthouse.beep.model.gifticon.GifticonBarcodeType
import com.lighthouse.beep.ui.dialog.textinput.TextInputParam
import com.lighthouse.beep.ui.feature.editor.R

@Suppress("unused")
internal enum class EditType(
    @StringRes val textResId: Int,
    @StringRes val hintResId: Int = 0,
) {
    MEMO(
        textResId = R.string.editor_gifticon_property_memo,
        hintResId = R.string.editor_gifticon_preview_memo_hint,
    ) {
        override fun createEditDataWithText(value: String): EditData {
            return EditData.Memo(value)
        }

        override fun createTextInputParam(data: GifticonData?): TextInputParam {
            return TextInputParam(
                text = data?.memo ?: "",
                maxLength = 25,
                inputFormat = TextInputFormat.TEXT,
            )
        }

        override fun getText(data: GifticonData): String {
            return data.memo
        }
    },
    THUMBNAIL(
        textResId = R.string.editor_gifticon_property_thumbnail,
    ) {
        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: Rect, zoom: Float): EditData {
            return EditData.CropThumbnail(bitmap, rect, zoom)
        }

        override fun getCropData(data: GifticonData?): GifticonCropData {
            return data?.thumbnailCropData ?: GifticonCropData.None
        }
    },
    NAME(
        textResId = R.string.editor_gifticon_property_name,
        hintResId = R.string.editor_gifticon_preview_name_hint,
    ) {
        private val textRecognizer = TextRecognizer()

        override fun createEditDataWithText(value: String): EditData {
            return EditData.Name(value)
        }

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: Rect, zoom: Float): EditData {
            val value = textRecognizer.recognize(bitmap).joinToString("")
            return EditData.CropName(value, rect, zoom)
        }

        override fun createTextInputParam(data: GifticonData?): TextInputParam {
            return TextInputParam(
                text = data?.name ?: "",
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
    BRAND(
        textResId = R.string.editor_gifticon_property_brand,
        hintResId = R.string.editor_gifticon_preview_brand_hint,
    ) {
        private val textRecognizer = TextRecognizer()

        override fun createEditDataWithText(value: String): EditData {
            return EditData.Brand(value)
        }

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: Rect, zoom: Float): EditData {
            val value = textRecognizer.recognize(bitmap).joinToString("")
            return EditData.CropBrand(value, rect, zoom)
        }

        override fun createTextInputParam(data: GifticonData?): TextInputParam {
            return TextInputParam(
                text = data?.brand ?: "",
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
    BARCODE(
        textResId = R.string.editor_gifticon_property_barcode,
        hintResId = R.string.editor_gifticon_preview_barcode_hint,
    ) {
        private val barcodeRecognizer = BarcodeRecognizer()

        override fun createEditDataWithText(value: String): EditData {
            return EditData.Barcode(
                barcodeType = GifticonBarcodeType.CODE_128,
                barcode = value,
            )
        }

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: Rect, zoom: Float): EditData {
            val info = barcodeRecognizer.recognize(bitmap)
            return EditData.CropBarcode(
                barcodeType = when(info.type) {
                    Barcode.FORMAT_QR_CODE -> GifticonBarcodeType.QR_CODE
                    else -> GifticonBarcodeType.CODE_128
                },
                barcode = info.barcode,
                rect = rect,
                zoom = zoom,
            )
        }

        override fun createTextInputParam(data: GifticonData?): TextInputParam {
            return TextInputParam(
                text = data?.barcode ?: "",
                inputFormat = TextInputFormat.BARCODE,
            )
        }

        override fun isInvalid(data: GifticonData): Boolean {
            return data.barcode.length < 12
        }

        override fun getText(data: GifticonData): String {
            return data.displayBarcode
        }

        override fun getCropData(data: GifticonData?): GifticonCropData {
            return data?.barcodeCropData ?: GifticonCropData.None
        }
    },
    EXPIRED(
        textResId = R.string.editor_gifticon_property_expired,
        hintResId = R.string.editor_gifticon_preview_expired_hint,
    ) {
        private val expiredRecognizer = ExpiredRecognizer()

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: Rect, zoom: Float): EditData {
            val value = expiredRecognizer.recognize(bitmap)
            return EditData.CropExpired(value, rect, zoom)
        }

        override fun getText(data: GifticonData): String {
            return data.displayExpired
        }

        override fun getCropData(data: GifticonData?): GifticonCropData {
            return data?.expireAtCropData ?: GifticonCropData.None
        }

        override fun isInvalid(data: GifticonData): Boolean {
            return data.displayExpired.isEmpty()
        }
    },
    BALANCE(
        textResId = R.string.editor_gifticon_property_balance,
        hintResId = R.string.editor_gifticon_preview_balance_hint,
    ) {
        private val balanceRecognizer = BalanceRecognizer()

        override fun createEditDataWithText(value: String): EditData {
            return EditData.Balance(value)
        }

        override suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: Rect, zoom: Float): EditData {
            val value = balanceRecognizer.recognize(bitmap).toString()
            return EditData.CropBalance(value, rect, zoom)
        }

        override fun createTextInputParam(data: GifticonData?): TextInputParam {
            return TextInputParam(
                text = data?.balance ?: "",
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

    open fun createEditDataWithText(value: String): EditData = EditData.None

    open suspend fun createEditDataWithCrop(bitmap: Bitmap, rect: Rect, zoom: Float): EditData =
        EditData.None

    open fun createTextInputParam(data: GifticonData?) = TextInputParam.None

    open fun isInvalid(data: GifticonData): Boolean = false

    open fun getText(data: GifticonData): String = ""

    open fun getCropData(data: GifticonData?): GifticonCropData = GifticonCropData.None
}