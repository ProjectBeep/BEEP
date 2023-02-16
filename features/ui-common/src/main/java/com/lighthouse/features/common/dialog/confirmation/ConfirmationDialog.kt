package com.lighthouse.features.common.dialog.confirmation

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.lighthouse.features.common.R
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.common.databinding.DialogConfirmationBinding

class ConfirmationDialog : DialogFragment(R.layout.dialog_confirmation) {

    private val binding: DialogConfirmationBinding by viewBindings()

    private var initTitle: String? = null
    private var initMessage: String? = null
    private var initOkText: String? = null
    private var initCancelText: String? = null

    fun setTitle(title: String) {
        initTitle = title
    }

    fun setMessage(message: String) {
        initMessage = message
    }

    fun setOkText(okText: String) {
        initOkText = okText
    }

    fun setCancelText(cancelText: String) {
        initCancelText = cancelText
    }

    private var onOkClickListener: OnClickListener? = null
    fun setOnOkClickListener(listener: (() -> Unit)?) {
        onOkClickListener = listener?.let {
            OnClickListener { it() }
        }
    }

    private var onCancelClickListener: OnClickListener? = null
    fun setOnCancelListener(listener: (() -> Unit)?) {
        onCancelClickListener = listener?.let {
            OnClickListener { it() }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvTitle.setTextWithVisible(initTitle)
        binding.tvMessage.setTextWithVisible(initMessage)

        setUpRoot()
        setUpOkButton()
        setUpCancelButton()
    }

    private fun setUpRoot() {
        binding.root.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpOkButton() {
        binding.tvOk.apply {
            setTextWithVisible(initOkText)
            setOnClickListener { v ->
                if (onOkClickListener != null) {
                    onOkClickListener?.onClick(v)
                }
                dismiss()
            }
        }
    }

    private fun setUpCancelButton() {
        binding.tvCancel.apply {
            setTextWithVisible(initCancelText)
            setOnClickListener { v ->
                if (onCancelClickListener != null) {
                    onCancelClickListener?.onClick(v)
                }
                dismiss()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.apply {
            attributes = attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
            }
        }
    }

    private fun TextView.setTextWithVisible(text: String?) {
        this.text = text ?: this.text ?: ""
        isVisible = this.text != ""
    }
}
