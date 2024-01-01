package com.lighthouse.beep.ui.feature.archive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.beep.auth.BeepAuth
import com.lighthouse.beep.data.repository.gifticon.GifticonRepository
import com.lighthouse.beep.model.gifticon.GifticonSortBy
import com.lighthouse.beep.ui.feature.archive.model.GifticonViewMode
import com.lighthouse.beep.ui.feature.archive.model.UsedGifticonItem
import com.lighthouse.beep.ui.feature.archive.model.toUsedModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ArchiveViewModel @Inject constructor(
    private val gifticonRepository: GifticonRepository
) : ViewModel() {

    val usedGifticonList = BeepAuth.currentUid.flatMapLatest { userUid ->
        gifticonRepository.getGifticonList(
            userId = userUid,
            isUsed = true,
            gifticonSortBy = GifticonSortBy.UPDATE,
            isAsc = false
        ).map {
            it.toUsedModel()
        }
    }

    private val _gifticonViewMode = MutableStateFlow(GifticonViewMode.VIEW)
    val gifticonViewMode = _gifticonViewMode.asStateFlow()

    fun toggleGifticonViewModel() {
        val nextViewMode = when (gifticonViewMode.value) {
            GifticonViewMode.VIEW -> GifticonViewMode.EDIT
            GifticonViewMode.EDIT -> GifticonViewMode.VIEW
        }
        setGifticonViewModel(nextViewMode)
    }

    private fun setGifticonViewModel(mode: GifticonViewMode) {
        if (mode == GifticonViewMode.VIEW) {
            initSelectedGifticonList()
        }
        _gifticonViewMode.value = mode
    }

    private val selectedGifticonList = mutableListOf<UsedGifticonItem>()
    val selectedCount
        get() = selectedGifticonList.size

    private val _selectedGifticonListFlow =
        MutableSharedFlow<List<UsedGifticonItem>>(replay = 1)
    val selectedGifticonListFlow = _selectedGifticonListFlow.asSharedFlow()

    fun selectGifticon(item: UsedGifticonItem) {
        if (selectedGifticonList.find { it.id == item.id } != null) {
            selectedGifticonList.remove(item)
        } else {
            selectedGifticonList.add(item)
        }
        viewModelScope.launch {
            _selectedGifticonListFlow.emit(selectedGifticonList)
        }
    }
    private fun initSelectedGifticonList() {
        selectedGifticonList.clear()
        viewModelScope.launch {
            _selectedGifticonListFlow.emit(emptyList())
        }
    }

    fun deleteSelectedGifticon() {
        viewModelScope.launch {
            val gifticonIdList = selectedGifticonList.map { it.id }
            gifticonRepository.deleteGifticon(BeepAuth.userUid, gifticonIdList)
            setGifticonViewModel(GifticonViewMode.VIEW)
        }
    }

    fun revertSelectedGifticon() {
        viewModelScope.launch {
            val gifticonIdList = selectedGifticonList.map { it.id }
            gifticonRepository.revertGifticonList(BeepAuth.userUid, gifticonIdList)
            setGifticonViewModel(GifticonViewMode.VIEW)
        }
    }

    init {
        initSelectedGifticonList()
    }
}