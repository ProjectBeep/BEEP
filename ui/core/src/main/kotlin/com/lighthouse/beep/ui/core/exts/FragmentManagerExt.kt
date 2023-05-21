package com.lighthouse.beep.ui.core.exts

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import kotlin.reflect.KClass

inline fun <reified T : Fragment> FragmentManager.findFragment(clazz: KClass<T>): Fragment? {
    return findFragmentByTag(clazz::class.java.name)
}

inline fun <reified T : DialogFragment> FragmentManager.findDialogFragment(clazz: KClass<T>): DialogFragment? {
    return findFragmentByTag(clazz::class.java.name) as? DialogFragment
}
