package com.lighthouse.features.common.ext

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

fun DialogFragment.show(fragmentManager: FragmentManager) {
    show(fragmentManager, javaClass.name)
}
