package com.lighthouse.beep.ui.dialog.gifticondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.core.common.utils.flow.MutableEventFlow
import com.lighthouse.beep.core.common.utils.flow.asEventFlow
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GifticonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val gifticonRepository: GifticonRepository,
) : ViewModel() {

    private val gifticonId = GifticonDetailParam.getGifticonId(savedStateHandle)

    val gifticonDetail = flow {
        val gifticon = gifticonRepository.getGifticonDetail(BeepAuth.userUid, gifticonId)
        _isUsed.value = gifticon?.isUsed
        _remainCash.value = gifticon?.remainCash
        emit(gifticon)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _isUsed = MutableStateFlow<Boolean?>(null)
    val isUsed = _isUsed.asStateFlow()

    private val _remainCash = MutableStateFlow<Int?>(null)
    val remainCash = _remainCash.asStateFlow()

    private val _requestDismissEvent = MutableEventFlow<Unit>()
    val requestDismissEvent = _requestDismissEvent.asEventFlow()

    fun deleteGifticon() {
        viewModelScope.launch {
            gifticonRepository.deleteGifticon(BeepAuth.userUid, listOf(gifticonId))
            _requestDismissEvent.emit(Unit)
        }
    }

    fun saveGifticon() {
        viewModelScope.launch {
            val detail = gifticonDetail.value
            val isUsed = isUsed.value ?: false
            val remainCash = remainCash.value ?: 0
            if (detail == null || (detail.isUsed == isUsed && detail.remainCash == remainCash)) {
                _requestDismissEvent.emit(Unit)
                return@launch
            }

            gifticonRepository.updateGifticonUseInfo(
                BeepAuth.userUid,
                gifticonId,
                isUsed,
                remainCash
            )
            _requestDismissEvent.emit(Unit)
        }
    }

    fun useGifticon() {
        _isUsed.value = true
        _remainCash.value = 0
    }

    fun revertUseGifticon() {
        _isUsed.value = false
        _remainCash.value = gifticonDetail.value?.totalCash ?: 0
    }

    fun useCash(cash: Int) {
        val remain = remainCash.value ?: return
        if (remain <= cash) {
            _remainCash.value = 0
            _isUsed.value = true
        }
    }
}