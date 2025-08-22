package com.lighthouse.beep.ui.dialog.withdrawal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.theme.R
import com.lighthouse.beep.ui.dialog.withdrawal.databinding.DialogWithdrawalBinding

class WithdrawalDialog : DialogFragment() {

    companion object {
        const val TAG = "Withdrawal"
    }

    private var _binding: DialogWithdrawalBinding? = null
    private val binding: DialogWithdrawalBinding
        get() = requireNotNull(_binding)

    private val viewModel by viewModels<WithdrawalViewModel>()

    private var onOkClickListener: View.OnClickListener? = null
    fun setOnOkClickListener(listener: View.OnClickListener?) {
        onOkClickListener = listener
    }

    private var onCancelClickListener: View.OnClickListener? = null
    fun setOnCancelClickListener(listener: View.OnClickListener?) {
        onCancelClickListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogWithdrawalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpCollectState()
        setUpOnClickEvent()
    }

    private fun setUpCollectState() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.isConfirm.collect {
                binding.btnOk.isEnabled = it
                binding.iconConfirm.isSelected = it
            }
        }
    }

    private fun setUpOnClickEvent() {
        binding.iconConfirm.setOnThrottleClickListener {
            viewModel.toggleConfirm()
        }

        binding.textConfirm.setOnThrottleClickListener {
            viewModel.toggleConfirm()
        }

        binding.btnCancel.setOnThrottleClickListener {
            onCancelClickListener?.onClick(binding.btnCancel)
            dismiss()

        }

        binding.btnOk.setOnThrottleClickListener {
            onOkClickListener?.onClick(binding.btnOk)
            dismiss()
        }
    }
}