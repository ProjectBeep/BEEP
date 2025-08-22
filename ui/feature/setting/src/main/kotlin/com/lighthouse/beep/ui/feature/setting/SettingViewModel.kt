package com.lighthouse.beep.ui.feature.setting

import androidx.lifecycle.ViewModel
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
internal class SettingViewModel @Inject constructor(
    private val gifticonRepository: GifticonRepository
) : ViewModel() {

    val usableGifticonCount = BeepAuth.currentUid.flatMapLatest {
        gifticonRepository.getGifticonCount(it, false)
    }

    val usedGifticonCount = BeepAuth.currentUid.flatMapLatest {
        gifticonRepository.getGifticonCount(it, true)
    }
}