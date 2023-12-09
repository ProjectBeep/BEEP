package com.lighthouse.beep.ui.dialog.confirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.lighthouse.beep.core.ui.exts.preventTouchPropagation
import com.lighthouse.beep.theme.R
import com.lighthouse.beep.ui.dialog.confirmation.databinding.DialogConfirmationBinding

class ConfirmationDialog : DialogFragment() {

    companion object {
        const val TAG = "Confirmation"

        fun newInstance(params: ConfirmationParam): ConfirmationDialog {
            return ConfirmationDialog().apply {
                arguments = params.buildBundle()
            }
        }
    }

    private var _binding: DialogConfirmationBinding? = null

    private val binding: DialogConfirmationBinding
        get() = requireNotNull(_binding)

    private var onOkClickListener: OnClickListener? = null
    fun setOnOkClickListener(listener: OnClickListener?) {
        onOkClickListener = listener
    }

    private var onCancelClickListener: OnClickListener? = null
    fun setOnCancelListener(listener: OnClickListener?) {
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
        _binding = DialogConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRoot()
        setUpContent()
    }

    private fun setUpRoot() {
        binding.root.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpContent() {
        binding.containerContent.preventTouchPropagation()

        binding.tvMessage.apply {
            text = ConfirmationParam.getMessage(arguments)
            isVisible = text.isNotEmpty()
        }

        binding.tvOk.apply {
            text = ConfirmationParam.getOkText(arguments)
            isVisible = text.isNotEmpty()
            setOnClickListener { v ->
                if (onOkClickListener != null) {
                    onOkClickListener?.onClick(v)
                }
                dismiss()
            }
        }

        binding.tvCancel.apply {
            text = ConfirmationParam.getCancelText(arguments)
            isVisible = text.isNotEmpty()
            setOnClickListener { v ->
                if (onCancelClickListener != null) {
                    onCancelClickListener?.onClick(v)
                }
                dismiss()
            }
        }
    }
}
