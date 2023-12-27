package com.lighthouse.beep.ui.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.ui.feature.home.model.HomePageState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val gifticonRepository: GifticonRepository,
) : ViewModel() {

    val homePageState = BeepAuth.currentUid.flatMapLatest {
        if (it.isEmpty()) {
            flow { emit(HomePageState.NONE) }
        } else {
            gifticonRepository.isExistGifticon(it, false).map { isExist ->
                when (isExist) {
                    true -> HomePageState.MAIN
                    false -> HomePageState.EMPTY
                }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, HomePageState.NONE)

    val isExistUsedGifticon = BeepAuth.currentUid.flatMapLatest {
        gifticonRepository.isExistGifticon(BeepAuth.userUid, true)
    }
}