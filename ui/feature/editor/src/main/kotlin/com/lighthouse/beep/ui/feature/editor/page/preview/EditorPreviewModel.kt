package com.lighthouse.beep.ui.feature.editor.page.preview

import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.library.barcode.BarcodeGenerator
import com.lighthouse.beep.ui.feature.editor.EditorPreviewViewModelDelegate
import com.lighthouse.beep.ui.feature.editor.model.EditType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class EditorPreviewModel constructor(
    editorPreviewViewModelDelegate: EditorPreviewViewModelDelegate
) : EditorPreviewViewModelDelegate by editorPreviewViewModelDelegate {

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
}
