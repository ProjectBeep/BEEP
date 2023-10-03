package com.lighthouse.beep.ui.feature.editor.model

import androidx.fragment.app.Fragment
import com.lighthouse.beep.ui.feature.editor.page.crop.EditorCropFragment
import com.lighthouse.beep.ui.feature.editor.page.preview.EditorPreviewFragment

enum class EditorPage {
    PREVIEW{
        override fun createFragment(): Fragment {
            return EditorPreviewFragment()
        }
    },
    CROP{
        override fun createFragment(): Fragment {
            return EditorCropFragment()
        }
    };

    abstract fun createFragment(): Fragment
}