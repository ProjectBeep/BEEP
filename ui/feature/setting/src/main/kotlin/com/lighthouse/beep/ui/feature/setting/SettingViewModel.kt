package com.lighthouse.beep.ui.feature.setting

import androidx.lifecycle.ViewModel
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SettingViewModel @Inject constructor(
    private val gifticonRepository: GifticonRepository
) : ViewModel() {

    val usableGifticonCount = gifticonRepository.getGifticonCount(BeepAuth.userUid, false)

    val usedGifticonCount = gifticonRepository.getGifticonCount(BeepAuth.userUid, true)
}