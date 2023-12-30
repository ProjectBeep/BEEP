package com.lighthouse.beep.ui.dialog.textinput

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.Window
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.ui.binding.viewBindings
import com.lighthouse.beep.core.ui.keyboard.addKeyboardHeightCallback
import com.lighthouse.beep.library.textformat.setInputFormat
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

    private val binding by viewBindings<DialogTextInputBinding> {
        keyboardAnimator?.cancel()
    }

    private val viewModel by viewModels<TextInputViewModel>()

    private val imm by lazy {
        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                requestFeature(Window.FEATURE_NO_TITLE)
                setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING or
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
                )
                setOnKeyListener { _, keyCode, _ ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
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

    private var keyboardAnimator: ViewPropertyAnimator? = null

    private fun setUpKeyboardPadding() {
        addKeyboardHeightCallback { height ->
            if (!isAdded) {
                return@addKeyboardHeightCallback
            }
            val start = binding.containerTextInput.paddingBottom
            keyboardAnimator = binding.containerTextInput.animate()
                .setDuration(180)
                .setInterpolator(DecelerateInterpolator())
                .setUpdateListener {
                    if (isAdded) {
                        val padding = start - ((start - height) * it.animatedFraction).toInt()
                        binding.containerTextInput.updatePadding(bottom = padding)
                    }
                }.also {
                    it.start()
                }
        }
    }

    private fun setUpEditText() {
        binding.editText.setText(viewModel.displayText)
        binding.editText.hint = viewModel.hint
        binding.editText.imeOptions = EditorInfo.IME_ACTION_DONE

        binding.editText.setInputFormat(
            viewModel.inputFormat,
            viewModel.filters
        ) { newValue ->
            viewModel.setValue(newValue)
        }

//        binding.editText.filters = viewModel.filters
//        binding.editText.inputType = viewModel.inputType
//        binding.editText.setRawInputType(viewModel.rawInputType)
//        binding.editText.hint = viewModel.hint
//        binding.editText.imeOptions = EditorInfo.IME_ACTION_DONE
//
//        binding.editText.setText(viewModel.displayText)
//
//        if (viewModel.inputFormat == TextInputFormat.TEXT) {
//            binding.editText.addTextChangedListener {
//                viewModel.setValue(it.toString())
//            }
//        } else {
//            binding.editText.addTextChangedListener(object : SimpleTextWatcher() {
//                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                    val newString = s.toString()
//                    val oldDisplayText = viewModel.displayText
//                    if (oldDisplayText == newString) {
//                        return
//                    }
//                    val newValue = if (
//                        before == 1 && count == 0 &&
//                        start < oldDisplayText.length &&
//                        oldDisplayText[start] == viewModel.separator
//                    ) {
//                        val prefix = newString.substring(0, Integer.max(start - 1, 0))
//                        val text = newString.substring(Integer.max(start, 0), newString.length)
//                        viewModel.inputFormat.transformedToValue(prefix + text)
//                    } else {
//                        viewModel.inputFormat.transformedToValue(newString)
//                    }
//                    viewModel.setValue(newValue)
//                    val newDisplayText = viewModel.displayText
//                    val selection = if (oldDisplayText.length == start + before) {
//                        newDisplayText.length
//                    } else {
//                        val separator = viewModel.separator
//                        val endStringCount = max(oldDisplayText.length - start - before, 0)
//                        val oldSeparatorCount = oldDisplayText
//                            .substring(start + before, oldDisplayText.length)
//                            .count { it == separator }
//                        val endNumCount = max(endStringCount - oldSeparatorCount, 0)
//                        var index = 0
//                        var numCount = 0
//                        while (
//                            newDisplayText.lastIndex - index >= 0 &&
//                            (numCount < endNumCount || newDisplayText[newDisplayText.lastIndex - index] == separator)
//                        ) {
//                            if (newDisplayText[newDisplayText.lastIndex - index] != separator) {
//                                numCount += 1
//                            }
//                            index += 1
//                        }
//                        newDisplayText.lastIndex - index + 1
//                    }
//                    setText(newDisplayText, selection)
//                }
//
//                private fun setText(text: String, selection: Int) {
//                    binding.editText.setText(text)
//                    Selection.setSelection(binding.editText.text, min(selection, text.length))
//                }
//            })
//        }
        binding.editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideDialog()
                true
            } else {
                false
            }
        }
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
        val result = TextInputResult(viewModel.displayText, viewModel.value)
        setFragmentResult(TextInputResult.KEY, result.buildBundle())

        super.onDismiss(dialogInterface)
    }
}