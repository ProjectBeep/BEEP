package com.lighthouse.beep.ui.dialog.gifticondetail.usecash

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.ui.exts.setOnThrottleClickListener
import com.lighthouse.beep.library.textformat.TextInputFormat
import com.lighthouse.beep.library.textformat.setInputFormat
import com.lighthouse.beep.theme.R as ThemeR
import com.lighthouse.beep.ui.dialog.gifticondetail.databinding.DialogGifticonUseCashBinding

class GifticonUseCashDialog : DialogFragment() {

    companion object {
        const val TAG = "UseCash"

        fun newInstance(param: GifticonUseCashParam): GifticonUseCashDialog {
            return GifticonUseCashDialog().apply {
                arguments = param.buildBundle()
            }
        }
    }

    private var onDismissListener: OnDismissListener? = null

    fun setOnDismissListener(listener: OnDismissListener) {
        onDismissListener = listener
    }

    private var onUseCashListener: OnUseCashListener? = null

    fun setOnUseCashListener(listener: OnUseCashListener) {
        onUseCashListener = listener
    }

    private var _binding: DialogGifticonUseCashBinding? = null
    private val binding: DialogGifticonUseCashBinding
        get() = requireNotNull(_binding)

    private val viewModel by viewModels<GifticonUseCashViewModel>()

    private val format = TextInputFormat.BALANCE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, ThemeR.style.Theme_Dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogGifticonUseCashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpEditCash()
        setUpOnEventClick()
    }

    override fun onDismiss(dialog: DialogInterface) {
        val window = this.dialog?.window
        if (window != null) {
            WindowCompat.getInsetsController(window, binding.editUseCash)
                .hide(WindowInsetsCompat.Type.ime())
        }
        onDismissListener?.onDismiss()

        super.onDismiss(dialog)
    }

    private fun setUpEditCash() {
        binding.editUseCash.setText(viewModel.displayText)
        binding.editUseCash.hint = viewModel.displayText
        binding.editUseCash.imeOptions = EditorInfo.IME_ACTION_DONE

        binding.editUseCash.setInputFormat(
            inputFormat = format,
        ) { newValue ->
            viewModel.setValue(newValue)
        }

        binding.editUseCash.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                useCash()
                true
            } else {
                false
            }
        }

        binding.editUseCash.requestFocus()
        val window = dialog?.window ?: return
        WindowCompat.getInsetsController(window, binding.editUseCash)
            .show(WindowInsetsCompat.Type.ime())
    }

    private fun setUpOnEventClick() {
        binding.btnCancel.setOnThrottleClickListener {
            dismiss()
        }

        binding.btnOk.setOnThrottleClickListener {
            useCash()
        }
    }

    private fun useCash() {
        var useCash = viewModel.value.toInt()
        if (useCash == 0) {
           useCash = viewModel.remainCash
        }
        onUseCashListener?.onUseCash(useCash)
        dismiss()
    }
}