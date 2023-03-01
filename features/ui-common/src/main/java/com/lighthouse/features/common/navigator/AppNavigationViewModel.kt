package com.lighthouse.features.common.navigator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.core.utils.flow.MutableEventFlow
import com.lighthouse.core.utils.flow.asEventFlow
import com.lighthouse.features.common.model.NavigationItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor() : ViewModel() {

    private val _navigation = MutableEventFlow<NavigationItem>()
    val navigation = _navigation.asEventFlow()

    fun navigate(item: NavigationItem) {
        viewModelScope.launch {
            _navigation.emit(item)
        }
    }
}
