package com.lighthouse.beep.ui.dialog.withdrawal

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class WithdrawalViewModel: ViewModel() {

    private val _isConfirm = MutableStateFlow(false)
    val isConfirm = _isConfirm.asStateFlow()

    fun toggleConfirm() {
        _isConfirm.value = !isConfirm.value
    }
}