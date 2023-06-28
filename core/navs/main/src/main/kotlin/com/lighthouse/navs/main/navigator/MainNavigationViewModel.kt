package com.lighthouse.navs.main.navigator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.core.utils.flow.MutableEventFlow
import com.lighthouse.core.utils.flow.asEventFlow
import com.lighthouse.navs.main.model.MainNavigationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainNavigationViewModel @Inject constructor() : ViewModel() {

    private val _navigation = MutableEventFlow<MainNavigationItem>()
    val navigation = _navigation.asEventFlow()

    fun navigate(item: MainNavigationItem) {
        viewModelScope.launch {
            _navigation.emit(item)
        }
    }
}
