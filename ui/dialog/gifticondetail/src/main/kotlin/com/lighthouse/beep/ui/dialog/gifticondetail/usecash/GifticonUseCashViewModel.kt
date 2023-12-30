package com.lighthouse.beep.ui.dialog.gifticondetail.usecash

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.lighthouse.beep.library.textformat.TextInputFormat

class GifticonUseCashViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    val remainCash = GifticonUseCashParam.getRemainCash(savedStateHandle)

    val inputFormat = TextInputFormat.BALANCE

    var displayText = inputFormat.valueToTransformed(remainCash.toString())
        private set

    var value = remainCash.toString()
        private set

    fun setValue(newValue: String) {
        displayText = inputFormat.valueToTransformed(newValue)
        value = newValue
    }
}