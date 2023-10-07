package com.lighthouse.beep.ui.feature.editor.adapter.preview

import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.library.barcode.BarcodeGenerator
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class EditorPreviewDisplayModel(
    gifticonDataFlow: Flow<GifticonData>,
) {

    val thumbnailCropData = gifticonDataFlow
        .map { it.thumbnailCropData }
        .distinctUntilChanged()

    val gifticonName = gifticonDataFlow
        .map { it.name }
        .distinctUntilChanged()

    val brandName = gifticonDataFlow
        .map { it.brand }
        .distinctUntilChanged()

    val displayExpired = gifticonDataFlow
        .map { it.displayExpired }
        .distinctUntilChanged()

    val isCash = gifticonDataFlow
        .map { it.isCashCard }
        .distinctUntilChanged()

    val displayBalance = gifticonDataFlow
        .map { it.displayBalance }
        .distinctUntilChanged()

    val barcodeImage = gifticonDataFlow
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

    val displayBarcode = gifticonDataFlow
        .map { it.displayBarcode }
        .distinctUntilChanged()
}