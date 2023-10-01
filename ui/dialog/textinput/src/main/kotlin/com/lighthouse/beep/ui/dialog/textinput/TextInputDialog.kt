package com.lighthouse.beep.ui.dialog.textinput

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.core.view.updatePadding
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.keyboard.keyboardProviders
import com.lighthouse.beep.ui.dialog.textinput.databinding.DialogTextInputBinding
import com.lighthouse.beep.theme.R as ThemeR

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
                requestFeature(Window.FEATURE_NO_TITLE)
                setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                )
                setOnKeyListener { _, keyCode, _ ->
                    if(keyCode == KeyEvent.KEYCODE_BACK){
                        hideDialog()
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, ThemeR.style.Theme_Dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpKeyboardPadding()
        setUpEditText()
        setUpClickEvent()
    }

    private fun setUpKeyboardPadding() {
        keyboardHeightProvider.setOnKeyboardHeightListener { height ->
            if (isAdded) {
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
        }
    }

    private fun setUpEditText() {
        binding.editText.addTextChangedListener {
            viewModel.text = it.toString()
        }
        binding.editText.setText(TextInputParam.getText(arguments))
        binding.editText.hint = TextInputParam.getHint(arguments)
        binding.editText.requestFocus()
    }

    private fun setUpClickEvent() {
        binding.root.setOnClickListener {
            hideDialog()
        }
    }

    private fun hideDialog() {
        val windowToken = binding.editText.windowToken
        if (windowToken != null) {
            imm?.hideSoftInputFromWindow(windowToken, 0)
        }
        dismiss()
    }

    override fun onDismiss(dialogInterface: DialogInterface) {
        val result = TextInputResult(viewModel.text)
        setFragmentResult(TextInputResult.KEY, result.buildBundle())

        super.onDismiss(dialogInterface)
    }
}