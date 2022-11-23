package com.lighthouse.presentation.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lighthouse.domain.model.CustomError
import com.lighthouse.domain.usecase.GetBrandPlaceInfosUseCase
import com.lighthouse.domain.usecase.GetUserLocationUseCase
import com.lighthouse.presentation.mapper.toPresentation
import com.lighthouse.presentation.model.BrandPlaceInfoUiModel
import com.lighthouse.presentation.ui.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getBrandPlaceInfosUseCase: GetBrandPlaceInfosUseCase,
    private val getUserLocation: GetUserLocationUseCase
) : ViewModel() {

    private val brandList = listOf("스타벅스", "베스킨라빈스", "BHC", "BBQ", "GS25", "CU", "서브웨이", "세븐일레븐", "파파존스")

    var state: MutableSharedFlow<UiState<List<BrandPlaceInfoUiModel>>> = MutableSharedFlow()
        private set

    init {
        collectLocation()
    }

    private fun collectLocation() {
        viewModelScope.launch {
            getUserLocation().collect {
                getBrandPlaceInfos(it.longitude, it.latitude)
            }
        }
    }

    fun getBrandPlaceInfos(x: Double, y: Double) {
        viewModelScope.launch {
            getBrandPlaceInfosUseCase(brandList, x, y, SEARCH_SIZE).collect { result ->
                result.mapCatching { it.toPresentation() }
                    .onSuccess { brandPlaceInfos ->
                        state.emit(UiState.Success(brandPlaceInfos))
                    }
                    .onFailure { throwable ->
                        Timber.tag("TAG").d("throwable - > $throwable")
                        state.emit(
                            when (throwable) {
                                CustomError.NetworkFailure -> UiState.NetworkFailure
                                CustomError.EmptyResults -> UiState.NotFoundResults
                                else -> UiState.Failure(throwable)
                            }
                        )
                    }
            }
        }
    }

    companion object {
        private const val SEARCH_SIZE = 10
    }
}
