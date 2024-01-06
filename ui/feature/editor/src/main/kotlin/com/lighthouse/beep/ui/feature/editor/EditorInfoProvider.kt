package com.lighthouse.beep.ui.feature.editor

import com.lighthouse.beep.ui.feature.editor.dialog.BuiltInThumbnailListener
import com.lighthouse.beep.ui.feature.editor.feature.crop.EditorCropInfoListener

internal interface EditorInfoProvider {

    val cropInfoListener: EditorCropInfoListener

    val builtInThumbnailListener: BuiltInThumbnailListener
}