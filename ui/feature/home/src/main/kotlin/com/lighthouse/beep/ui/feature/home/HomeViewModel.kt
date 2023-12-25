package com.lighthouse.beep.ui.feature.home

import androidx.lifecycle.ViewModel
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val gifticonRepository: GifticonRepository,
) : ViewModel() {

    val isExistGifticon = gifticonRepository.isExistGifticon(BeepAuth.userUid, false)

    val isExistUsedGifticon = gifticonRepository.isExistGifticon(BeepAuth.userUid, true)

    val authInfoFlow = BeepAuth.authInfoFlow.filterNotNull()
}