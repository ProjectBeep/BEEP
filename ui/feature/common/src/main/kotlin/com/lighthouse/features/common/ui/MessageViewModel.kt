package com.lighthouse.features.common.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.core.android.utils.resource.UIText
import com.lighthouse.core.utils.flow.MutableEventFlow
import com.lighthouse.core.utils.flow.asEventFlow
import com.lighthouse.features.common.model.MessageEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor() : ViewModel() {

    private val _messageFlow = MutableEventFlow<MessageEvent>()

    val messageFlow = _messageFlow.asEventFlow()

    fun sendMessage(event: MessageEvent) {
        viewModelScope.launch {
            _messageFlow.emit(event)
        }
    }

    fun sendSnackBar(@StringRes resId: Int) {
        viewModelScope.launch {
            _messageFlow.emit(MessageEvent.SnackBar(UIText.StringResource(resId)))
        }
    }

    fun sendSnackBar(message: String) {
        viewModelScope.launch {
            _messageFlow.emit(MessageEvent.SnackBar(UIText.DynamicString(message)))
        }
    }

    fun sendSnackBar(text: UIText) {
        viewModelScope.launch {
            _messageFlow.emit(MessageEvent.SnackBar(text))
        }
    }

    fun sendToast(@StringRes resId: Int) {
        viewModelScope.launch {
            _messageFlow.emit(MessageEvent.Toast(UIText.StringResource(resId)))
        }
    }

    fun sendToast(message: String) {
        viewModelScope.launch {
            _messageFlow.emit(MessageEvent.Toast(UIText.DynamicString(message)))
        }
    }

    fun sendToast(text: UIText) {
        viewModelScope.launch {
            _messageFlow.emit(MessageEvent.Toast(text))
        }
    }
}
