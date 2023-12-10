package com.lighthouse.beep.ui.feature.editor.adapter.preview

import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

internal class EditorPreviewDisplayModel(
    gifticonDataFlow: Flow<GifticonData>,
) {

    val thumbnailCropData = gifticonDataFlow
        .map { it.thumbnail }
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
        .map {
            when (EditType.BALANCE.isInvalid(it)) {
                true -> ""
                false -> it.displayBalance
            }
        }.distinctUntilChanged()

    val displayBarcode = gifticonDataFlow
        .map {
            when (EditType.BARCODE.isInvalid(it)) {
                true -> ""
                false -> it.displayBarcode
            }
        }.distinctUntilChanged()
}