package com.lighthouse.beep.ui.feature.editor.model

import android.graphics.Bitmap
import android.graphics.Rect
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail
import java.util.Date

@Suppress("unused")
internal sealed interface EditData {

    data object None : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return false
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data
        }
    }

    data class CropThumbnail(val bitmap: Bitmap, val rect: Rect, val zoom: Float) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            if (data.thumbnail !is EditGifticonThumbnail.Crop) {
                return true
            }
            return bitmap != data.thumbnail.bitmap ||
                    rect != data.thumbnailCropData.rect ||
                    zoom != data.thumbnailCropData.zoom
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(
                thumbnail = EditGifticonThumbnail.Crop(
                    bitmap = bitmap,
                    rect = rect
                ),
                thumbnailCropData = GifticonCropData(
                    bitmap = bitmap,
                    rect = rect,
                    zoom = zoom,
                )
            )
        }
    }

    data class BuiltInThumbnail(
        val builtIn: GifticonBuiltInThumbnail
    ) : EditData {

        override fun isModified(data: GifticonData): Boolean {
            if (data.thumbnail !is EditGifticonThumbnail.BuiltIn) {
                return true
            }
            return data.thumbnail.builtIn != builtIn
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(
                thumbnail = EditGifticonThumbnail.BuiltIn(
                    builtIn = builtIn
                ),
            )
        }
    }

    data object ClearThumbnail : EditData {
        override fun isModified(data: GifticonData): Boolean = true

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(
                thumbnail = data.thumbnailCropData.bitmap?.let { cropBitmap ->
                    EditGifticonThumbnail.Crop(
                        bitmap = cropBitmap,
                        rect = data.thumbnailCropData.rect,
                    )
                } ?: EditGifticonThumbnail.Default(data.originUri)
            )
        }
    }

    data class Name(val name: String) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.name != name
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(name = name)
        }
    }

    data class CropName(val name: String, val rect: Rect, val zoom: Float) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.name != name ||
                    data.nameCropData.rect != rect ||
                    data.nameCropData.zoom != zoom
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(
                name = name,
                nameCropData = GifticonCropData(
                    rect = rect,
                    zoom = zoom,
                )
            )
        }
    }

    data class Brand(val brand: String) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.brand != brand
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(brand = brand)
        }
    }

    data class CropBrand(val brand: String, val rect: Rect, val zoom: Float) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.brand != brand ||
                    data.brandCropData.rect != rect ||
                    data.brandCropData.zoom != zoom
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(
                brand = brand,
                brandCropData = GifticonCropData(
                    rect = rect,
                    zoom = zoom,
                )
            )
        }
    }

    data class Barcode(val barcode: String) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.barcode != barcode
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(barcode = barcode)
        }
    }

    data class CropBarcode(val barcode: String, val rect: Rect, val zoom: Float) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.barcode != barcode ||
                    data.barcodeCropData.rect != rect ||
                    data.barcodeCropData.zoom != zoom
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(
                barcode = barcode,
                barcodeCropData = GifticonCropData(
                    rect = rect,
                    zoom = zoom,
                )
            )
        }
    }

    data class Expired(val date: Date) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.expireAt != date
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(expireAt = date)
        }
    }

    data class CropExpired(val date: Date, val rect: Rect, val zoom: Float) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.expireAt != date ||
                    data.expireAtCropData.rect != rect ||
                    data.expireAtCropData.zoom != zoom
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(
                expireAt = date,
                expireAtCropData = GifticonCropData(
                    rect = rect,
                    zoom = zoom
                )
            )
        }
    }

    data class Cash(val isCash: Boolean) : EditData {

        override fun isModified(data: GifticonData): Boolean {
            return data.isCashCard != isCash
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(isCashCard = isCash)
        }
    }

    data class Balance(val balance: String) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.balance != balance
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(balance = balance)
        }
    }

    data class CropBalance(val balance: String, val rect: Rect, val zoom: Float) : EditData {
        override fun isModified(data: GifticonData): Boolean {
            return data.balance != balance ||
                    data.balanceCropData.rect != rect ||
                    data.balanceCropData.zoom != zoom
        }

        override fun updatedGifticon(data: GifticonData): GifticonData {
            return data.copy(
                balance = balance,
                balanceCropData = GifticonCropData(
                    rect = rect,
                    zoom = zoom,
                )
            )
        }
    }

    data class Memo(val memo: String) : EditData {
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