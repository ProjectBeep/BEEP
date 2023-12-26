package com.lighthouse.beep.ui.feature.setting

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.lighthouse.beep.auth.AuthActivity
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.navs.ActivityNavItem
import com.lighthouse.beep.navs.AppNavigator
import com.lighthouse.beep.ui.feature.setting.databinding.ActivitySettingBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@AndroidEntryPoint
internal class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    private val viewModel by viewModels<SettingViewModel>()

    @Inject
    lateinit var navigator: AppNavigator

    private val requestManager by lazy {
        Glide.with(this)
    }

    private val accountLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                AuthActivity.RESULT_OK -> {
                    gotoLoginAndClearTask()
                }
                AuthActivity.RESULT_CANCELED -> {

                }
                AuthActivity.RESULT_FAILED -> {

                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        setUpContainer()
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpContainer() {


        binding.containerGroupEtc.clipToOutline = true
        binding.containerGroupAccount.clipToOutline = true
    }

    private fun setUpCollectState() {
        repeatOnStarted {
            BeepAuth.authInfoFlow.filterNotNull().collect {
                requestManager.load(it.photoUrl)
                    .placeholder(com.lighthouse.beep.theme.R.drawable.icon_default_profile)
                    .transform(CircleCrop())
                    .into(binding.imageProfile)

                binding.textProfileName.text = it.displayName
                binding.textProfileEmail.text = it.email

                if (it.provider.iconResId == 0) {
                    binding.imageProfile.setImageDrawable(null)
                } else {
                    binding.imageProfile.setImageResource(it.provider.iconResId)
                }

                binding.btnAccountLogout.isVisible = it.provider.isAvailableLogout
                binding.dividerGroupAccount.isVisible = it.provider.isAvailableLogout
            }
        }

        repeatOnStarted {
            viewModel.usableGifticonCount.collect { count ->
                binding.textUsableGifticonCount.text =
                    getString(R.string.setting_gifticon_count, count)
            }
        }

        repeatOnStarted {
            viewModel.usedGifticonCount.collect { count ->
                binding.textUsedGifticonCount.text =
                    getString(R.string.setting_gifticon_count, count)
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnEtcPersonalInfoPolicy.setOnThrottleClickListener {

        }

        binding.btnEtcOpenSourceLibrary.setOnThrottleClickListener {

        }

        binding.btnAccountLogout.setOnThrottleClickListener {
            accountLauncher.launch(BeepAuth.getSignOutIntent(this))
        }

        binding.btnAccountWithdrawal.setOnThrottleClickListener {
            accountLauncher.launch(BeepAuth.getWithdrawalIntent(this))
        }
    }

    private fun gotoLoginAndClearTask() {
        val intent = navigator.getIntent(this, ActivityNavItem.Login(true))
        startActivity(intent)
    }
}