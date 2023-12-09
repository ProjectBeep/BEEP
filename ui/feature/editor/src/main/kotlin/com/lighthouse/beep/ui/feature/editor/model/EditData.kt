package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.Bitmap
import android.graphics.RectF
import java.util.Date

@Suppress("unused")
internal sealed interface EditData {

    data object None: EditData {
        override fun isModified(data: GifticonData): Boolean {
            return false
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data
        }
    }

    data class Thumbnail(val bitmap: Bitmap, val rect: RectF): EditData {
        override fun isModified(data: GifticonData): Boolean {
            if (data.thumbnail !is GifticonThumbnail.Crop) {
                return true
            }
            return bitmap != data.thumbnail.bitmap && rect != data.thumbnail.rect
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(thumbnail = GifticonThumbnail.Crop(
                bitmap = bitmap,
                rect = rect
            ))
        }
    }

    data class Name(val name: String): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.name != name
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(name = name)
        }
    }

    data class CropName(val name: String, val rect: RectF): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.name != name || data.nameRect != rect
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(name = name, nameRect = rect)
        }
    }

    data class Brand(val brand: String): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.brand != brand
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(brand = brand)
        }
    }

    data class CropBrand(val brand: String, val rect: RectF): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.brand != brand || data.brandRect != rect
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(brand = brand, brandRect = rect)
        }
    }

    data class Barcode(val barcode: String): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.barcode != barcode
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(barcode = barcode)
        }
    }

    data class CropBarcode(val barcode: String, val rect: RectF): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.barcode != barcode || data.barcodeRect != rect
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(barcode = barcode, barcodeRect = rect)
        }
    }

    data class Expired(val date: Date): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.expired != date
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(expired = date)
        }
    }

    data class CropExpired(val date: Date, val rect: RectF): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.expired != date || data.expiredRect != rect
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(expired = date, expiredRect = rect)
        }
    }

    data class Cash(val isCash: Boolean): EditData {

        override fun isModified(data: GifticonData): Boolean {
            return data.isCashCard != isCash
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(isCashCard = isCash)
        }
    }

    data class Balance(val balance: String): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.balance != balance
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(balance = balance)
        }
    }

    data class CropBalance(val balance: String, val rect: RectF): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.balance != balance || data.balanceRect != rect
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(balance = balance, balanceRect = rect)
        }
    }

    data class Memo(val memo: String): EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.memo != memo
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(memo = memo)
        }
    }

    fun isModified(data: GifticonData): Boolean

    fun updatedGifticon(data: GifticonData): GifticonData
}