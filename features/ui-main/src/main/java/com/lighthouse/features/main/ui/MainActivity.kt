package com.lighthouse.features.main.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.lighthouse.core.android.utils.permission.StoragePermissionManager
import com.lighthouse.core.android.utils.permission.core.permissions
import com.lighthouse.features.main.R
import com.lighthouse.features.main.databinding.ActivityMainBinding
import com.lighthouse.features.main.navigator.MainNav
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    private val storagePermission: StoragePermissionManager by permissions()

    @Inject
    lateinit var nav: MainNav

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv) as NavHostFragment

        val navController = navHostFragment.navController
        binding.bnv.setupWithNavController(navController)
    }
}
