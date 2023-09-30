package com.lighthouse.beep.ui.dialog.textinput

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.keyboard.keyboardProviders
import com.lighthouse.beep.ui.dialog.textinput.databinding.DialogTextInputBinding

class TextInputDialog : DialogFragment(R.layout.dialog_text_input) {

    companion object {
        const val TAG = "TextInputDialog'"

        fun newInstance(param: TextInputParam): TextInputDialog {
            return TextInputDialog().apply {
                arguments = param.buildBundle()
            }
        }
    }

    private val binding by viewBindings<DialogTextInputBinding>()

    private val viewModel by viewModels<TextInputViewModel>()

    private val imm by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    }

    private val keyboardHeightProvider by keyboardProviders()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.editText.addTextChangedListener {
            viewModel.text = it.toString()
        }
        binding.editText.setText(TextInputParam.getText(arguments))
        binding.editText.hint = TextInputParam.getHint(arguments)
        binding.editText.requestFocus()

        imm?.showSoftInput(binding.editText, 0)

        binding.root.setOnClickListener {
            dismiss()
        }

        keyboardHeightProvider.setOnKeyboardHeightListener { height ->
            val start = binding.containerTextInput.paddingBottom
            binding.containerTextInput.animate()
                .setDuration(180)
                .setInterpolator(DecelerateInterpolator())
                .setUpdateListener {
                    val padding = start - ((start - height) * it.animatedFraction).toInt()
                    binding.containerTextInput.updatePadding(bottom = padding)
                }
                .start()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            dismiss()
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

    override fun onDismiss(dialog: DialogInterface) {
        val result = TextInputResult(viewModel.text)
        setFragmentResult(TextInputResult.KEY, result.buildBundle())

        super.onDismiss(dialog)
    }
}