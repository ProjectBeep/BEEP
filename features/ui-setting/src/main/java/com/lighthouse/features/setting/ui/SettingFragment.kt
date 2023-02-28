package com.lighthouse.features.setting.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.adapter.SettingAdapter
import com.lighthouse.features.setting.databinding.FragmentSettingBinding
import com.lighthouse.features.setting.model.SettingMenu
import com.lighthouse.features.setting.navigator.SettingNav
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding: FragmentSettingBinding by viewBindings()

    private val viewModel: SettingViewModel by viewModels()

    private val adapter = SettingAdapter(
        onClick = { menu ->
            setUpMenuOnClick(menu)
        },
        onCheckedChange = { menu, isChecked ->
            setUpMenuOnCheckedChange(menu, isChecked)
        }
    )

    @Inject
    lateinit var nav: SettingNav

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setUpSettingMenu() {
    }

    private fun setUpMenuOnClick(menu: SettingMenu) {
        when (menu) {
            SettingMenu.USED_GIFTICON -> {}
            SettingMenu.SECURITY -> {}
            SettingMenu.LOCATION -> {}
            SettingMenu.SIGN_IN -> {}
            SettingMenu.SIGN_OUT -> {}
            SettingMenu.WITHDRAWAL -> {}
            SettingMenu.COFFEE -> {}
            SettingMenu.TERMS_OF_USE -> {}
            SettingMenu.PERSONAL_INFO_POLICY -> {}
            SettingMenu.OPEN_SOURCE_LICENSE -> {}
            else -> Unit
        }
    }

    private fun setUpMenuOnCheckedChange(menu: SettingMenu, isChecked: Boolean) {
        when (menu) {
            SettingMenu.IMMINENT_NOTIFICATION -> viewModel.setNotificationEnable(isChecked)
            else -> Unit
        }
    }
}
