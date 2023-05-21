package com.lighthouse.features.main.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lighthouse.beep.ui.core.binding.viewBindings
import com.lighthouse.beep.ui.core.exts.repeatOnStarted
import com.lighthouse.beep.ui.page.main.R
import com.lighthouse.beep.ui.page.main.databinding.FragmentMainContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainContainerFragment : Fragment(R.layout.fragment_main_container) {

    private val binding: FragmentMainContainerBinding by viewBindings()

    private val viewModel: MainContainerViewModel by viewModels()

//    private val mainNavigationViewModel: MainNavigationViewModel by viewModels()

    private lateinit var navController: NavController
//    private val destinationChangedListener =
//        NavController.OnDestinationChangedListener { controller, destination, _ ->
//            backPressedCallback.isEnabled =
//                controller.graph.findStartDestination().id != destination.id
//
//            when (destination.id) {
//                R.id.home_container_fragment,
//                R.id.gifticon_list_fragment ->
//                    binding.fabAddGifticon.show()
//
//                else ->
//                    binding.fabAddGifticon.hide()
//            }
//        }

    private val backPressedCallback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            navController.popBackStack()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

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
//        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    private fun setUpBottomNavigation() {
        binding.bnv.setupWithNavController(navController)
    }

    private fun setUpNavigation() {
        viewLifecycleOwner.repeatOnStarted {
//            mainNavigationViewModel.navigation.collect { item ->
//                when (item) {
//                    MainNavigationItem.Popup -> navController.popBackStack()
//                    else -> Unit
//                }
//            }
        }
    }

    override fun onDestroyView() {
//        navController.removeOnDestinationChangedListener(destinationChangedListener)

        super.onDestroyView()
    }
}
