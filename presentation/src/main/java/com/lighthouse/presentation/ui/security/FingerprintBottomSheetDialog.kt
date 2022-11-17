package com.lighthouse.presentation.ui.security

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lighthouse.presentation.R
import com.lighthouse.presentation.databinding.DialogFingerprintLegacyBinding
import com.lighthouse.presentation.ui.security.fingerprint.FingerprintAuthCallback

class FingerprintBottomSheetDialog(
    private val fingerprintAuthCallback: FingerprintAuthCallback
) : BottomSheetDialogFragment() {
    private lateinit var binding: DialogFingerprintLegacyBinding
    private val shakeAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.anim_shake)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_fingerprint_legacy, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnFingerprintCancel.setOnClickListener {
            dismiss()
            fingerprintAuthCallback.onBiometricAuthCancel()
        }
    }

    fun failAuthentication() {
        binding.ivFingerprint.startAnimation(shakeAnimation)
        binding.tvFingerprintSubtitle.apply {
            text = "지문 인증에 실패했습니다."
            setTextColor(resources.getColor(R.color.beep_pink, requireActivity().theme))
        }
    }

    fun showErrorMessage(@StringRes id: Int) {
        binding.tvFingerprintSubtitle.apply {
            text = resources.getText(id)
            setTextColor(resources.getColor(R.color.black, requireActivity().theme))
        }
        binding.ivFingerprint.setColorFilter(resources.getColor(R.color.gray_500, requireActivity().theme))
    }
}
