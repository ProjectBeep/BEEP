package com.lighthouse.features.setting.ui

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.lighthouse.auth.google.repository.GoogleClient
import com.lighthouse.beep.model.auth.AuthProvider
import com.lighthouse.core.android.utils.permission.LocationPermissionManager
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.common.dialog.progress.ProgressDialog
import com.lighthouse.features.common.ext.repeatOnStarted
import com.lighthouse.features.common.ext.show
import com.lighthouse.features.common.ui.AuthViewModel
import com.lighthouse.features.common.ui.MessageViewModel
import com.lighthouse.features.setting.R
import com.lighthouse.features.setting.adapter.SettingAdapter
import com.lighthouse.features.setting.databinding.FragmentSettingBinding
import com.lighthouse.features.setting.model.SettingMenu
import com.lighthouse.navs.app.model.AppNavigationItem
import com.lighthouse.navs.app.navigator.AppNavigationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {

    private val binding: FragmentSettingBinding by viewBindings()

    private val viewModel: SettingViewModel by viewModels()

    private val messageViewModel: MessageViewModel by activityViewModels()

    private val appNavigationViewModel: AppNavigationViewModel by activityViewModels()

    private val settingAdapter = SettingAdapter(
        onClick = { menu ->
            setUpMenuOnClick(menu)
        },
        onCheckedChange = { menu, isChecked ->
            setUpMenuOnCheckedChange(menu, isChecked)
        }
    )

    private val authViewModel: AuthViewModel by viewModels()

    @Inject
    lateinit var googleClient: GoogleClient

    private var progressDialog: ProgressDialog? = null

    private val googleLoginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            lifecycleScope.launch {
                try {
                    val credential = googleClient.getAuthCredential(result).getOrThrow()
                    authViewModel.login(AuthProvider.GOOGLE, credential)
                } catch (e: Exception) {
                    authViewModel.endLogin(e)
                }
            }
        }

    private val locationPermissionSettingLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val isGrant = locationPermissionManager.isGrant()
            if (!isGrant) {
                messageViewModel.sendSnackBar(R.string.error_permission_not_allowed)
            }
            viewModel.setLocationEnable(isGrant)
        }

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { results ->
            val isGrant = results.all { it.value }
            if (!isGrant) {
                messageViewModel.sendSnackBar(R.string.error_permission_not_allowed)
            }
            viewModel.setLocationEnable(isGrant)
        }

    private val locationPermissionManager by lazy {
        LocationPermissionManager(requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpLocationPermission()
        setUpSettingMenu()
        setUpGoogleAuthEvent()
        setUpSignInLoading()
    }

    private fun setUpLocationPermission() {
        viewModel.setLocationEnable(locationPermissionManager.isGrant())
    }

    private fun setUpSettingMenu() {
        binding.rvList.adapter = settingAdapter
        viewLifecycleOwner.repeatOnStarted {
            viewModel.settingMenus.collect { menus ->
                settingAdapter.submitList(menus)
            }
        }
    }

    private fun setUpGoogleAuthEvent() {
        viewLifecycleOwner.repeatOnStarted {
            authViewModel.eventFlow.collect { event ->
                messageViewModel.sendMessage(event)
            }
        }
    }

    private fun setUpSignInLoading() {
        repeatOnStarted {
            authViewModel.loginLoading.collect { loading ->
                if (loading) {
                    if (progressDialog?.isAdded == true) {
                        progressDialog?.dismiss()
                    }
                    progressDialog = ProgressDialog()
                    progressDialog?.show(childFragmentManager)
                } else {
                    progressDialog?.dismiss()
                }
            }
        }
    }

    private fun setUpMenuOnClick(menu: SettingMenu) {
        when (menu) {
            SettingMenu.USED_GIFTICON ->
                appNavigationViewModel.navigate(AppNavigationItem.UsedGifticon)

            SettingMenu.SECURITY -> gotoSecurity()
            SettingMenu.LOCATION -> gotoLocation()
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

    private fun gotoSecurity() {
        appNavigationViewModel.navigate(AppNavigationItem.Security)
    }

    private fun gotoLocation() {
        if (
            locationPermissionManager.isGrant() ||
            locationPermissionManager.getManyTimesRejectedPermission() != null
        ) {
            locationPermissionSettingLauncher.launch(locationPermissionManager.settingIntent())
        } else {
            locationPermissionLauncher.launch(locationPermissionManager.getPermissions())
        }
    }

    private fun signIn() {
        authViewModel.startLogin()
        googleLoginLauncher.launch(googleClient.signInIntent())
    }

    private fun signOut() {
        authViewModel.signOut()
    }

    private fun withdrawal() {
        authViewModel.withdrawal()
    }

    private fun setUpMenuOnCheckedChange(menu: SettingMenu, isChecked: Boolean) {
        when (menu) {
            SettingMenu.IMMINENT_NOTIFICATION -> viewModel.setNotificationEnable(isChecked)
            else -> Unit
        }
    }
}
