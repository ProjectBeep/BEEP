package com.lighthouse.beep.ui.feature.editor.page.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.library.barcode.BarcodeGenerator
import com.lighthouse.beep.ui.feature.editor.EditorSelectGifticonDataDelegate
import com.lighthouse.beep.ui.feature.editor.model.EditType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class EditorPreviewViewModel constructor(
    editorSelectGifticonDataDelegate: EditorSelectGifticonDataDelegate
) : ViewModel(), EditorSelectGifticonDataDelegate by editorSelectGifticonDataDelegate {

    companion object {
        fun factory(delegate: EditorSelectGifticonDataDelegate) = viewModelFactory {
            initializer {
                EditorPreviewViewModel(delegate)
            }
        }
    }

    val thumbnailUri = selectedGifticonDataFlow
        .map { it.originUri }
        .distinctUntilChanged()

    val thumbnailCropData = selectedGifticonDataFlow
        .map { it.thumbnailCropData }
        .distinctUntilChanged()

    val gifticonName = selectedGifticonDataFlow
        .map { it.name }
        .distinctUntilChanged()

    val brandName = selectedGifticonDataFlow
        .map { it.brand }
        .distinctUntilChanged()

    val displayExpired = selectedGifticonDataFlow
        .map { it.displayExpired }
        .distinctUntilChanged()

    val displayBalance = selectedGifticonDataFlow
        .map { it.displayBalance }
        .distinctUntilChanged()

    val barcodeImage = selectedGifticonDataFlow
        .map { EditType.BARCODE.isInvalid(it) to it.displayBarcode }
        .distinctUntilChanged()
        .map { (isInvalid, displayBarcode) ->
            when(isInvalid) {
                true -> null
                false -> withContext(Dispatchers.IO) {
                    BarcodeGenerator.generate(displayBarcode, 300.dp, 60.dp)
                }
            }
        }

    val displayBarcode = selectedGifticonDataFlow
        .map { it.displayBarcode }
        .distinctUntilChanged()

//    private fun createBarcode(value: String): Bitmap {
//        val width = 300.dp
//        val height = 60.dp
//        val bitMatrix = Code128Writer().encode(value, BarcodeFormat.CODE_128, width, height)
//
//        val pixels = IntArray(width * height) { i ->
//            val y = i / width
//            val x = i % width
//            if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
//        }
//
//        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//            .apply {
//                setPixels(pixels, 0, width, 0, 0, width, height)
//            }
//    }
}
