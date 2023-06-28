package com.lighthouse.beep.core.ui.exts

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.show(fragmentManager: FragmentManager) {
    show(fragmentManager, javaClass.name)
}
