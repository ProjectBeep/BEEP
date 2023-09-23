package com.lighthouse.beep.ui.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.lighthouse.beep.core.ui.exts.setUpSystemInsetsPadding
import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredGifticonTabAdapter
import com.lighthouse.beep.ui.feature.home.adapter.expired.ExpiredOrder
import com.lighthouse.beep.ui.feature.home.databinding.ActivityHomeBinding
import com.lighthouse.beep.ui.feature.home.databinding.TabExpiredBinding
import com.lighthouse.beep.theme.R as ThemeR

class HomeActivity: AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val tabAdapter by lazy {
        ExpiredGifticonTabAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpSystemInsetsPadding(binding.root)
        setUpPager()
    }

    private fun setUpPager() {
        binding.pagerExpired.adapter = tabAdapter
        binding.tabExpired.addOnTabSelectedListener(object: OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val tabBinding = tab?.tag as? TabExpiredBinding ?: return
                tabBinding.textTitle.setTextAppearance(ThemeR.style.Text_Body1)
                tabBinding.textTitle.setTextColor(getColor(ThemeR.color.black))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val tabBinding = tab?.tag as? TabExpiredBinding ?: return
                tabBinding.textTitle.setTextAppearance(ThemeR.style.Text_Body2)
                tabBinding.textTitle.setTextColor(getColor(ThemeR.color.font_medium_gray))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
        TabLayoutMediator(binding.tabExpired, binding.pagerExpired) { tab, position ->
            val tabBinding = TabExpiredBinding.inflate(LayoutInflater.from(this))
            tab.tag = tabBinding
            tabBinding.textTitle.text = getString(ExpiredOrder.entries[position].titleRes)
            tab.customView = tabBinding.root
        }.attach()
    }
}