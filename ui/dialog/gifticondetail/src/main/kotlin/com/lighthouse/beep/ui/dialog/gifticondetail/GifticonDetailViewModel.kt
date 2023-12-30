package com.lighthouse.beep.ui.dialog.gifticondetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.core.common.utils.flow.MutableEventFlow
import com.lighthouse.beep.core.common.utils.flow.asEventFlow
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
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
        emit(gifticonRepository.getGifticonDetail(BeepAuth.userUid, gifticonId))
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isCashCard
        get() = gifticonDetail.value?.isCashCard ?: false

    val remainCash
        get() = gifticonDetail.value?.remainCash ?: 0

    val isUsed
        get() = gifticonDetail.value?.isUsed ?: false

    private val _requestDismissEvent = MutableEventFlow<Unit>()
    val requestDismissEvent = _requestDismissEvent.asEventFlow()

    fun deleteGifticon() {
        viewModelScope.launch {
            gifticonRepository.deleteGifticon(BeepAuth.userUid, listOf(gifticonId))
            _requestDismissEvent.emit(Unit)
        }
    }

    fun useGifticon() {
        viewModelScope.launch {
            gifticonRepository.updateGifticonUseInfo(BeepAuth.userUid, gifticonId, true, 0)
            _requestDismissEvent.emit(Unit)
        }
    }

    fun revertUseGifticon() {
        val gifticon = gifticonDetail.value ?: return
        viewModelScope.launch {
            gifticonRepository.updateGifticonUseInfo(
                BeepAuth.userUid,
                gifticonId,
                false,
                gifticon.totalCash
            )
            _requestDismissEvent.emit(Unit)
        }
    }

    fun useCash(cash: Int) {
        val gifticon = gifticonDetail.value ?: return
        viewModelScope.launch {
            val remainCash = maxOf(gifticon.remainCash - cash, 0)
            val isUsed = remainCash == 0
            gifticonRepository.updateGifticonUseInfo(
                BeepAuth.userUid,
                gifticonId,
                isUsed,
                remainCash
            )
            _requestDismissEvent.emit(Unit)
        }
    }
}