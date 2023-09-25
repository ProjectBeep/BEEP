package com.lighthouse.beep.ui.feature.home.adapter.expired

import com.lighthouse.beep.ui.feature.home.model.ExpiredBrandItem
import kotlinx.coroutines.flow.Flow

internal interface OnExpiredBrandListener {

    fun getSelectedFlow(): Flow<ExpiredBrandItem>
}