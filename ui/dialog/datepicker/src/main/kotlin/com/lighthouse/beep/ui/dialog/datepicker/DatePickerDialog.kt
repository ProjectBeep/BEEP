package com.lighthouse.beep.ui.dialog.datepicker

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
        val yearList = (2013 .. 2033).toList()
        yearAdapter.submitList(yearList)

        binding.spinnerYear.apply {
            adapter = yearAdapter
            layoutManager = DatePickerLayoutManager(context) {
                Log.d("TEST", "selected Year : ${yearList[it]}")
            }
        }

        val monthList = (1 .. 12).toList()
        monthAdapter.submitList(monthList)

        binding.spinnerMonth.apply {
            adapter = monthAdapter
            layoutManager = DatePickerLayoutManager(context) {
                Log.d("TEST", "selected Month : ${monthList[it]}")
            }
        }

        val dateList = (1 .. 31).toList()
        dateAdapter.submitList(dateList)

        binding.spinnerDate.apply {
            adapter = dateAdapter
            layoutManager = DatePickerLayoutManager(context) {
                Log.d("TEST", "selected Date : ${dateList[it]}")
            }
        }
    }
}
