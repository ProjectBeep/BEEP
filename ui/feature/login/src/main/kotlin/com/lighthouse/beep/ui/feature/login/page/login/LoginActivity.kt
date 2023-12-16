package com.lighthouse.beep.ui.feature.login.page.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lighthouse.beep.core.ui.exts.checkSelfPermissions
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.setUpSystemInsetsPadding
import com.lighthouse.beep.permission.BeepPermission
import com.lighthouse.beep.navs.ActivityNavItem
import com.lighthouse.beep.navs.AppNavigator
import com.lighthouse.beep.ui.designsystem.dotindicator.AutoPagerSnapHelper
import com.lighthouse.beep.ui.feature.login.databinding.ActivityLoginBinding
import com.lighthouse.beep.ui.feature.login.page.login.adapter.AppDescriptionAdapter
import com.lighthouse.beep.ui.feature.login.page.login.model.AppDescription
import com.lighthouse.beep.ui.feature.login.page.permission.RequestPermissionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var navigator: AppNavigator

    private val appDescriptionAdapter = AppDescriptionAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpSystemInsetsPadding(binding.root)
        setUpAppDescriptionList()
        setUpOnClickEvent()
    }

    private fun setUpAppDescriptionList() {
        binding.listDescription.apply {
            adapter = appDescriptionAdapter
        }
        appDescriptionAdapter.submitList(AppDescription.values().toList())

        AutoPagerSnapHelper().attachToRecyclerView(this, binding.listDescription)
        binding.indicator.attachToRecyclerView(binding.listDescription)
    }

    private fun setUpOnClickEvent() {
        binding.btnLoginGuest.setOnClickListener(createThrottleClickListener {
            lifecycleScope.launch {
                val isShownPermissionPage = viewModel.isShownPermissionPage.firstOrNull() ?: false
                if (!isShownPermissionPage && !checkSelfPermissions(BeepPermission.all)) {
                    gotoPermissionPage()
                } else {
                    gotoHomePage()
                }
            }
        })
    }

    private fun gotoPermissionPage() {
        viewModel.setShownPermissionPage(true)

        val intent = Intent(this, RequestPermissionActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun gotoHomePage() {
        val intent = navigator.getIntent(this, ActivityNavItem.Home)
        startActivity(intent)
        finish()
    }
}