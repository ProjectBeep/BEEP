package com.lighthouse.beep.ui.core.exts

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.show(fragmentManager: FragmentManager) {
    show(fragmentManager, javaClass.name)
}
