package com.lighthouse.beep.ui.dialog.datepicker

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.dialog.bottomsheet.BeepBottomSheetDialog
import com.lighthouse.beep.ui.dialog.datepicker.databinding.DialogSpinnerDatePickerBinding

class DatePickerDialog : BeepBottomSheetDialog() {

    companion object {
        fun newInstance(params: DatePickerParams): DatePickerDialog {
            return DatePickerDialog().apply {
                arguments = params.buildBundle()
            }
        }
    }

    private var _binding: DialogSpinnerDatePickerBinding? = null
    private val binding: DialogSpinnerDatePickerBinding
        get() = requireNotNull(_binding)

    private val viewModel: DatePickerViewModel by viewModels(
        factoryProducer = {
            DatePickerViewModel.factory
        },
    )

    private var onDatePickListener: OnDatePickListener? = null
    fun setOnDatePickerListener(listener: OnDatePickListener?) {
        onDatePickListener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogSpinnerDatePickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyContentView() {
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRoot()
        setUpPickers()
        setUpBtnOk()
        bindData()
    }

    private fun setUpRoot() {
        binding.root.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpBtnOk() {
        binding.btnOk.setOnClickListener {
            onDatePickListener?.onDatePick(viewModel.year, viewModel.month, viewModel.dayOfMonth)
            dismiss()
        }
    }

    private fun setUpPicker(
        picker: NumberPicker,
        min: Int,
        max: Int,
        initValue: Int,
        listener: OnValueChangeListener,
    ) {
        picker.apply {
            wrapSelectorWheel = false
            minValue = min
            maxValue = max
            value = initValue
            setOnValueChangedListener(listener)
        }
    }

    private fun setUpPickers() {
        setUpPicker(
            binding.npYear,
            DatePickerViewModel.MIN_YEAR,
            DatePickerViewModel.MAX_YEAR,
            viewModel.year,
        ) { _, _, newValue ->
            viewModel.year = newValue
        }

        setUpPicker(
            binding.npMonth,
            DatePickerViewModel.MIN_MONTH,
            DatePickerViewModel.MAX_MONTH,
            viewModel.month,
        ) { _, _, newValue ->
            viewModel.month = newValue
        }

        setUpPicker(
            binding.npDayOfMonth,
            DatePickerViewModel.MIN_DAY_OF_MONTH,
            DatePickerViewModel.MAX_DAY_OF_MONTH,
            viewModel.dayOfMonth,
        ) { _, _, newValue ->
            viewModel.dayOfMonth = newValue
        }
    }

    private fun bindData() {
        viewLifecycleOwner.repeatOnStarted {
            viewModel.maxDayOfMonth.collect { newValue ->
                binding.npDayOfMonth.maxValue = newValue
            }
        }
    }
}
