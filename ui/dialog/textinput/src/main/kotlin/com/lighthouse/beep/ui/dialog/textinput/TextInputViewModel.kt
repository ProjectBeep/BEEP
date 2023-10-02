package com.lighthouse.beep.ui.dialog.textinput

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

internal class TextInputViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val text = TextInputParam.getText(savedStateHandle)

    val hint = TextInputParam.getHint(savedStateHandle)

    private val maxLength = TextInputParam.getMaxLength(savedStateHandle)

    val inputFormat = TextInputParam.getInputFormat(savedStateHandle)

    val separator = inputFormat.separator

    val filters = mutableListOf<InputFilter>().apply {
        if (maxLength != Int.MAX_VALUE) {
            add(LengthFilter(maxLength))
        }
        addAll(inputFormat.filters)
    }.toTypedArray()

    val inputType = inputFormat.inputType

    val rawInputType = inputFormat.rawInputType

    var displayText = inputFormat.valueToTransformed(text)
        private set

    var value = inputFormat.transformedToValue(text)
        private set

    fun setValue(newValue: String) {
        displayText = inputFormat.valueToTransformed(newValue)
        value = newValue
    }
}