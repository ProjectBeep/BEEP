package com.lighthouse.features.setting.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.lighthouse.auth.google.repository.GoogleClient
import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.common.ext.repeatOnStarted
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.adapter.SettingAdapter
import com.lighthouse.features.setting.databinding.FragmentSettingBinding
import com.lighthouse.features.setting.model.SettingEvent
import com.lighthouse.features.setting.model.SettingMenu
import com.lighthouse.navs.app.model.AppNavigationItem
import com.lighthouse.navs.app.navigator.AppNavigationViewModel
import com.lighthouse.navs.main.model.MainNavigationItem
import com.lighthouse.navs.main.navigator.MainNavigationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding: FragmentSettingBinding by viewBindings()

    private val viewModel: SettingViewModel by viewModels()

    private val appNavigationViewModel: AppNavigationViewModel by activityViewModels()

    private val mainNavigationViewModel: MainNavigationViewModel by viewModels(
        ownerProducer = {
            var parent = requireParentFragment()
            while (parent is NavHostFragment) {
                parent = parent.requireParentFragment()
            }
            parent
        }
    )

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

    override fun onAttach(context: Context) {
        super.onAttach(context)

        setUpOnBackPressed()
    }

    private fun setUpOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            mainNavigationViewModel.navigate(MainNavigationItem.Popup)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpSettingMenu()
        setUpEvent()
    }

    private fun setUpSettingMenu() {
        binding.rvList.adapter = settingAdapter
        viewLifecycleOwner.repeatOnStarted {
            viewModel.settingMenus.collect { menus ->
                settingAdapter.submitList(menus)
            }
        }
    }

    private fun setUpEvent() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is SettingEvent.SnackBar -> showSnackBar(event.text)
                }
            }
        }
    }

    private fun setUpMenuOnClick(menu: SettingMenu) {
        when (menu) {
            SettingMenu.USED_GIFTICON ->
                appNavigationViewModel.navigate(AppNavigationItem.UsedGifticon)

            SettingMenu.SECURITY ->
                appNavigationViewModel.navigate(AppNavigationItem.Security)

            SettingMenu.LOCATION -> location()
            SettingMenu.SIGN_IN -> signIn()
            SettingMenu.SIGN_OUT -> signOut()
            SettingMenu.WITHDRAWAL -> withdrawal()
            SettingMenu.COFFEE ->
                appNavigationViewModel.navigate(AppNavigationItem.Coffee)

            SettingMenu.TERMS_OF_USE ->
                appNavigationViewModel.navigate(AppNavigationItem.TermsOfUse)

            SettingMenu.PERSONAL_INFO_POLICY ->
                appNavigationViewModel.navigate(AppNavigationItem.PersonalInfoPolicy)

            SettingMenu.OPEN_SOURCE_LICENSE ->
                appNavigationViewModel.navigate(AppNavigationItem.OpensourceLicense)

            else -> Unit
        }
    }

    private fun location() {
    }

    private fun signIn() {
    }

    private fun signOut() {
        lifecycleScope.launch {
            val exception = googleClient.signOut().exceptionOrNull()
            if (exception != null) {
                showSnackBar(getString(R.string.user_sign_out_exception))
            }
            viewModel.signOut()
        }
    }

    private fun withdrawal() {
        lifecycleScope.launch {
            val exception = googleClient.signOut().exceptionOrNull()
            if (exception != null) {
                showSnackBar(getString(R.string.user_withdrawal_exception))
            }
            viewModel.withdrawal()
        }
    }

    private fun setUpMenuOnCheckedChange(menu: SettingMenu, isChecked: Boolean) {
        when (menu) {
            SettingMenu.IMMINENT_NOTIFICATION -> viewModel.setNotificationEnable(isChecked)
            else -> Unit
        }
    }

    private fun showSnackBar(string: String) {
        Snackbar.make(binding.root, string, Snackbar.LENGTH_SHORT).show()
    }

    private fun showSnackBar(uiText: UIText) {
        showSnackBar(uiText.asString(requireContext()).toString())
    }
}
