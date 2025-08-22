package com.lighthouse.beep.ui.feature.editor.feature.crop

import com.lighthouse.beep.ui.feature.editor.model.EditData
import com.lighthouse.beep.ui.feature.editor.model.EditType
import com.lighthouse.beep.ui.feature.editor.model.EditorChip
import com.lighthouse.beep.ui.feature.editor.model.EditorGifticonInfo
import com.lighthouse.beep.ui.feature.editor.model.GifticonData
import kotlinx.coroutines.flow.Flow

internal interface EditorCropInfoListener {

    val selectedEditType: EditType?

    val selectedGifticonData: GifticonData?

    val selectedGifticonFlow: Flow<EditorGifticonInfo>

    val selectedGifticonDataFlow: Flow<GifticonData>

    val selectedPropertyChipFlow: Flow<EditorChip.Property>

    fun getGifticonData(id: Long): GifticonData?

    fun updateGifticonData(editData: EditData)
}