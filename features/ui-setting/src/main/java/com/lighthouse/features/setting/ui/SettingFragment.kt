package com.lighthouse.features.setting.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.databinding.FragmentSettingBinding
import com.lighthouse.features.setting.navigator.SettingNav
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding: FragmentSettingBinding by viewBindings()

    private val viewModel: SettingViewModel by viewModels()

    @Inject
    lateinit var nav: SettingNav

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
