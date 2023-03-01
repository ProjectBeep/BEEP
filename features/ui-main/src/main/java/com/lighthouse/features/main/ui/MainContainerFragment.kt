package com.lighthouse.features.main.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lighthouse.core.android.utils.permission.StoragePermissionManager
import com.lighthouse.core.android.utils.permission.core.permissions
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.common.ext.repeatOnStarted
import com.lighthouse.features.main.R
import com.lighthouse.features.main.databinding.FragmentMainContainerBinding
import com.lighthouse.navs.app.navigator.AppNavigationViewModel
import com.lighthouse.navs.main.model.MainNavigationItem
import com.lighthouse.navs.main.navigator.MainNavigationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainContainerFragment : Fragment(R.layout.fragment_main_container) {

    private val binding: FragmentMainContainerBinding by viewBindings()

    private val viewModel: MainContainerViewModel by viewModels()

    private val appNavigationViewModel: AppNavigationViewModel by activityViewModels()

    private val mainNavigationViewModel: MainNavigationViewModel by viewModels()

    private val storagePermission: StoragePermissionManager by permissions()

    private lateinit var navController: NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpNaController()
        setUpBottomNavigation()
        setUpNavigation()
    }

    private fun setUpNaController() {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setUpBottomNavigation() {
        binding.bnv.setupWithNavController(navController)
    }

    private fun setUpNavigation() {
        viewLifecycleOwner.repeatOnStarted {
            mainNavigationViewModel.navigation.collect { item ->
                when (item) {
                    MainNavigationItem.Popup -> navController.popBackStack()
                    else -> Unit
                }
            }
        }
    }
}
