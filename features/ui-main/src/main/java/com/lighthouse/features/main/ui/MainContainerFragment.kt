package com.lighthouse.features.main.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lighthouse.core.android.utils.permission.StoragePermissionManager
import com.lighthouse.core.android.utils.permission.core.permissions
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.main.R
import com.lighthouse.features.main.databinding.FragmentMainContainerBinding
import com.lighthouse.features.main.navigator.MainNav
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainContainerFragment : Fragment(R.layout.fragment_main_container) {

    private val binding by viewBindings<FragmentMainContainerBinding>()

    private val viewModel: MainContainerViewModel by viewModels()

    private val storagePermission: StoragePermissionManager by permissions()

    @Inject
    lateinit var nav: MainNav

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnv.setupWithNavController(navController)
    }
}
