package com.lighthouse.beep.ui.dialog.datepicker

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.lighthouse.beep.core.ui.exts.repeatOnStarted
import com.lighthouse.beep.ui.dialog.bottomsheet.BeepBottomSheetDialog
import com.lighthouse.beep.ui.dialog.datepicker.adapter.DatePickerAdapter
import com.lighthouse.beep.ui.dialog.datepicker.adapter.DatePickerLayoutManager
import com.lighthouse.beep.ui.dialog.datepicker.databinding.DialogSpinnerDatePickerBinding

class DatePickerDialog : BeepBottomSheetDialog() {

    companion object {
        const val TAG = "DatePicker"

        fun newInstance(params: DatePickerParam): DatePickerDialog {
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

    private val yearAdapter = DatePickerAdapter()
    private val monthAdapter = DatePickerAdapter()
    private val dateAdapter = DatePickerAdapter()

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
        attachHandle(binding.viewHandle)
        setUpSpinner()
    }

    private fun setUpSpinner() {
        yearAdapter.submitList(viewModel.yearList)

        val yearPosition = yearAdapter.getPosition(viewModel.year)
        binding.spinnerYear.apply {
            adapter = yearAdapter
            layoutManager = DatePickerLayoutManager(context) {
                viewModel.setYear(yearAdapter.getValue(it))
            }
            scrollToPosition(yearPosition)
        }

        monthAdapter.submitList(viewModel.monthList)

        val monthPosition = monthAdapter.getPosition(viewModel.month)
        binding.spinnerMonth.apply {
            adapter = monthAdapter
            layoutManager = DatePickerLayoutManager(context) {
                viewModel.setMonth(monthAdapter.getValue(it))
            }
            scrollToPosition(monthPosition)
        }

        viewLifecycleOwner.repeatOnStarted {
            viewModel.dateList.collect { dateList ->
                dateAdapter.submitList(dateList)

                val position = dateAdapter.getPosition(viewModel.dayOfMonth)
                binding.spinnerDate.scrollToPosition(position)
            }
        }

        binding.spinnerDate.apply {
            adapter = dateAdapter
            layoutManager = DatePickerLayoutManager(context) {
                viewModel.setDayOfMonth(dateAdapter.getValue(it))
            }
            itemAnimator = null
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        val result = DatePickerResult(viewModel.year, viewModel.month, viewModel.dayOfMonth)
        setFragmentResult(DatePickerResult.KEY, result.buildBundle())

        super.onDismiss(dialog)
    }
}
