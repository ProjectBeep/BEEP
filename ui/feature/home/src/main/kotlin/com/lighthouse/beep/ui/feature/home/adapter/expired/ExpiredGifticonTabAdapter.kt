package com.lighthouse.beep.ui.feature.home.adapter.expired

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lighthouse.beep.ui.feature.home.page.expired.ExpiredGifticonFragment
import com.lighthouse.beep.ui.feature.home.page.expired.ExpiredGifticonParam
import com.lighthouse.beep.ui.feature.home.page.expired.ExpiredOrder

class ExpiredGifticonTabAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return ExpiredOrder.entries.size
    }

    override fun createFragment(position: Int): Fragment {
        val param = ExpiredGifticonParam(ExpiredOrder.entries[position])
        return ExpiredGifticonFragment.newInstance(param)
    }
}