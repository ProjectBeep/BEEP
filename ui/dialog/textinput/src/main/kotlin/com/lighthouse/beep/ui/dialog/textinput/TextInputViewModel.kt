package com.lighthouse.beep.ui.dialog.textinput

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

internal class TextInputViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val text = TextInputParam.getText(savedStateHandle)

    val hint = TextInputParam.getHint(savedStateHandle)

    val inputFormat = TextInputParam.getInputFormat(savedStateHandle)

    val separator = inputFormat.separator

    var displayText = inputFormat.valueToTransformed(text)
        private set

    var value = inputFormat.transformedToValue(text)
        private set

    fun setValue(newValue: String) {
        displayText = inputFormat.valueToTransformed(newValue)
        value = newValue
    }
}