package com.lighthouse.features.common.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.NavHostFragment

inline fun <reified VM : ViewModel> Fragment.parentViewModels(): Lazy<VM> {
    return viewModels(
        ownerProducer = {
            var parent = requireParentFragment()
            while (parent.parentFragment is NavHostFragment) {
                parent = parent.requireParentFragment()
            }
            parent
        }
    )
}
