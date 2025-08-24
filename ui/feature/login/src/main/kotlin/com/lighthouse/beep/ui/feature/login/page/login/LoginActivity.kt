package com.lighthouse.beep.ui.feature.login.page.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lighthouse.beep.auth.AuthActivity
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.core.common.utils.log.MSLog
import com.lighthouse.beep.core.ui.exts.createThrottleClickListener
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.core.ui.exts.setUpSystemInsetsPadding
import com.lighthouse.beep.model.user.AuthProvider
import com.lighthouse.beep.permission.BeepPermission
import com.lighthouse.beep.navs.ActivityNavItem
import com.lighthouse.beep.navs.AppNavigator
import com.lighthouse.beep.permission.ext.checkSelfPermissions
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

    companion object {
        private const val TAG = "LoginActivity"
    }

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var navigator: AppNavigator

    private val appDescriptionAdapter = AppDescriptionAdapter()

    private val loginLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            MSLog.d(TAG, "loginLauncher 결과 수신 - resultCode: ${result.resultCode}")
            when(result.resultCode) {
                AuthActivity.RESULT_OK -> {
                    MSLog.d(TAG, "로그인 성공 - 권한 확인 및 페이지 이동 처리")
                    lifecycleScope.launch {
                        val isShownPermissionPage = viewModel.isShownPermissionPage.firstOrNull() ?: false
                        MSLog.d(TAG, "권한 페이지 표시 여부: $isShownPermissionPage")
                        val hasPermissions = checkSelfPermissions(BeepPermission.all)
                        MSLog.d(TAG, "권한 보유 여부: $hasPermissions")
                        
                        if (!isShownPermissionPage && !hasPermissions) {
                            MSLog.d(TAG, "권한 페이지로 이동")
                            gotoPermissionPage()
                        } else {
                            MSLog.d(TAG, "홈 페이지로 이동")
                            gotoHomePage()
                        }
                    }
                }
                AuthActivity.RESULT_CANCELED -> {
                    MSLog.d(TAG, "로그인 취소됨")
                }
                AuthActivity.RESULT_FAILED -> {
                    MSLog.e(TAG, "로그인 실패")
                }
                else -> {
                    MSLog.w(TAG, "알 수 없는 결과 코드: ${result.resultCode}")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MSLog.d(TAG, "LoginActivity onCreate 시작")
        binding = ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpSystemInsetsPadding(binding.root)
        setUpAppDescriptionList()
        setUpOnClickEvent()
        MSLog.d(TAG, "LoginActivity 초기화 완료")
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
        binding.btnLoginGoogle.setOnThrottleClickListener {
            MSLog.d(TAG, "Google 로그인 버튼 클릭")
            val intent = BeepAuth.getSignInIntent(this, AuthProvider.GOOGLE)
            MSLog.d(TAG, "Google 로그인 Intent 생성 완료 - AuthActivity 시작")
            loginLauncher.launch(intent)
        }

        binding.btnLoginGuest.setOnThrottleClickListener {
            MSLog.d(TAG, "Guest 로그인 버튼 클릭")
            val intent = BeepAuth.getSignInIntent(this, AuthProvider.GUEST)
            MSLog.d(TAG, "Guest 로그인 Intent 생성 완료 - AuthActivity 시작")
            loginLauncher.launch(intent)
        }
    }

    private fun gotoPermissionPage() {
        MSLog.d(TAG, "권한 페이지로 이동 시작")
        viewModel.setShownPermissionPage(true)

        val intent = Intent(this, RequestPermissionActivity::class.java)
        startActivity(intent)
        finish()
        MSLog.d(TAG, "권한 페이지 시작 및 LoginActivity 종료")
    }

    private fun gotoHomePage() {
        MSLog.d(TAG, "홈 페이지로 이동 시작")
        val intent = navigator.getIntent(this, ActivityNavItem.Home)
        startActivity(intent)
        finish()
        MSLog.d(TAG, "홈 페이지 시작 및 LoginActivity 종료")
    }
}