package com.lighthouse.features.setting.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.lighthouse.auth.google.repository.GoogleClient
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.common.ext.repeatOnStarted
import com.lighthouse.features.common.model.NavigationItem
import com.lighthouse.features.common.navigator.AppNavigationViewModel
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.adapter.SettingAdapter
import com.lighthouse.features.setting.databinding.FragmentSettingBinding
import com.lighthouse.features.setting.model.SettingMenu
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding: FragmentSettingBinding by viewBindings()

    private val viewModel: SettingViewModel by viewModels()

    private val appNavigationViewModel: AppNavigationViewModel by activityViewModels()

    private val settingAdapter = SettingAdapter(
        onClick = { menu ->
            setUpMenuOnClick(menu)
        },
        onCheckedChange = { menu, isChecked ->
            setUpMenuOnCheckedChange(menu, isChecked)
        }
    )

    @Inject
    lateinit var googleClient: GoogleClient

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSettingMenu()
    }

    private fun setUpSettingMenu() {
        binding.rvList.adapter = settingAdapter
        repeatOnStarted {
            viewModel.settingMenus.collect { menus ->
                settingAdapter.submitList(menus)
            }
        }
    }

    private fun setUpMenuOnClick(menu: SettingMenu) {
        when (menu) {
            SettingMenu.USED_GIFTICON ->
                appNavigationViewModel.navigate(NavigationItem.UsedGifticon)

            SettingMenu.SECURITY ->
                appNavigationViewModel.navigate(NavigationItem.Security)

            SettingMenu.LOCATION -> location()
            SettingMenu.SIGN_IN -> signIn()
            SettingMenu.SIGN_OUT -> signOut()
            SettingMenu.WITHDRAWAL -> withdrawal()
            SettingMenu.COFFEE ->
                appNavigationViewModel.navigate(NavigationItem.Coffee)

            SettingMenu.TERMS_OF_USE ->
                appNavigationViewModel.navigate(NavigationItem.TermsOfUse)

            SettingMenu.PERSONAL_INFO_POLICY ->
                appNavigationViewModel.navigate(NavigationItem.PersonalInfoPolicy)

            SettingMenu.OPEN_SOURCE_LICENSE ->
                appNavigationViewModel.navigate(NavigationItem.OpensourceLicense)

            else -> Unit
        }
    }

    private fun location() {
    }

    private fun signIn() {
    }

    private fun signOut() {
    }

    private fun withdrawal() {
    }

    private fun setUpMenuOnCheckedChange(menu: SettingMenu, isChecked: Boolean) {
        when (menu) {
            SettingMenu.IMMINENT_NOTIFICATION -> viewModel.setNotificationEnable(isChecked)
            else -> Unit
        }
    }
}
