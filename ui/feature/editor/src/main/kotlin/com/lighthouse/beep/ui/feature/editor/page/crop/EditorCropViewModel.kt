package com.lighthouse.beep.ui.feature.editor.page.crop

import androidx.lifecycle.ViewModel
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBalanceUseCase
import com.lighthouse.beep.domain.usecase.recognize.RecognizeBarcodeUseCase
import com.lighthouse.beep.domain.usecase.recognize.RecognizeExpiredUseCase
import com.lighthouse.beep.domain.usecase.recognize.RecognizeTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class EditorCropViewModel @Inject constructor(
    private val recognizeTextUseCase: RecognizeTextUseCase,
    private val recognizeBarcodeUseCase: RecognizeBarcodeUseCase,
    private val recognizeBalanceUseCase: RecognizeBalanceUseCase,
    private val recognizeExpiredUseCase: RecognizeExpiredUseCase,
): ViewModel() {
}