package com.lighthouse.beep.ui.feature.login.page.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.lighthouse.beep.core.ui.exts.checkSelfPermissions
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.setUpSystemInsetsPadding
import com.lighthouse.beep.library.permission.BeepPermission
import com.lighthouse.beep.navs.ActivityNavItem
import com.lighthouse.beep.navs.AppNavigator
import com.lighthouse.beep.ui.feature.login.databinding.ActivityLoginBinding
import com.lighthouse.beep.ui.feature.login.page.permission.RequestPermissionActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var navigator: AppNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpSystemInsetsPadding(binding.root)
        setUpOnClickEvent()
    }

    private fun setUpOnClickEvent() {
        binding.btnLoginGuest.setOnClickListener(createThrottleClickListener {
            if (checkSelfPermissions(BeepPermission.all)) {
                gotoHomePage()
            } else {
                gotoPermissionPage()
            }
        })
    }

    private fun gotoPermissionPage() {
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