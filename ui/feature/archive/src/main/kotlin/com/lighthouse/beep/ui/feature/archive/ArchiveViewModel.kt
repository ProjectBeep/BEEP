package com.lighthouse.beep.ui.feature.archive

import androidx.lifecycle.ViewModel
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import com.lighthouse.beep.ui.feature.archive.model.GifticonViewMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
internal class ArchiveViewModel @Inject constructor(
    private val gifticonRepository: GifticonRepository
) : ViewModel() {

    val usedGifticonCount = BeepAuth.currentUid.flatMapLatest { userUid ->
        gifticonRepository.getGifticonCount(userUid, true)
    }

    private val gifticonList = BeepAuth.currentUid.flatMapLatest { userUid ->
        gifticonRepository.getGifticonList(
            userId = userUid,
            isUsed = true,
            gifticonSortBy = GifticonSortBy.UPDATE,
            isAsc = false
        )
    }

    private val _gifticonViewMode = MutableStateFlow(GifticonViewMode.VIEW)
    val gifticonViewMode = _gifticonViewMode.asStateFlow()


}