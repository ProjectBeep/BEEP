package com.lighthouse.features.common.dialog.datepicker

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.lighthouse.core.exts.toDayOfMonth
import com.lighthouse.core.exts.toMonth
import com.lighthouse.core.exts.toYear
import com.lighthouse.features.common.R
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.common.databinding.DialogSpinnerDatePickerBinding
import java.util.Date

class SpinnerDatePicker : DialogFragment(R.layout.dialog_spinner_date_picker) {

    private val binding: DialogSpinnerDatePickerBinding by viewBindings()

    private var year: Int
    private var month: Int
    private var dayOfMonth: Int

    init {
        val date = Date()
        year = date.toYear()
        month = date.toMonth()
        dayOfMonth = date.toDayOfMonth()
    }

    fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        this.year = year
        this.month = month
        this.dayOfMonth = dayOfMonth
    }

    private var onDatePickListener: OnDatePickListener? = null
    fun setOnDatePickListener(listener: ((Int, Int, Int) -> Unit)?) {
        onDatePickListener = listener?.let {
            object : OnDatePickListener {
                override fun onDatePick(year: Int, month: Int, date: Int) {
                    it(year, month, date)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRoot()
        setUpYearNumberPicker()
        setUpMonthNumberPicker()
        setUpDayOfMonthNumberPicker()
        setUpBtnOk()
    }

    private fun setUpRoot() {
        binding.root.setOnClickListener {
            dismiss()
        }
    }

    private fun setUpBtnOk() {
        binding.btnOk.setOnClickListener {
            onDatePickListener?.onDatePick(year, month, dayOfMonth)
            dismiss()
        }
    }

    private fun setUpYearNumberPicker() {
        binding.npYear.apply {
            wrapSelectorWheel = false
            maxValue = MAX_YEAR
            minValue = MIN_YEAR
            value = year
            setOnValueChangedListener { _, _, newVal ->
                year = newVal
                binding.npDayOfMonth.maxValue = getMaxDayOfMonth(year, month)
            }
        }
    }

    private fun setUpMonthNumberPicker() {
        binding.npMonth.apply {
            wrapSelectorWheel = false
            maxValue = MAX_MONTH
            minValue = MIN_MONTH
            value = month
            setOnValueChangedListener { _, _, newVal ->
                month = newVal
                binding.npDayOfMonth.maxValue = getMaxDayOfMonth(year, month)
            }
        }
    }

    private fun setUpDayOfMonthNumberPicker() {
        binding.npDayOfMonth.apply {
            wrapSelectorWheel = false
            maxValue = MAX_DATE
            minValue = MIN_DATE
            value = dayOfMonth
            setOnValueChangedListener { _, _, newVal ->
                dayOfMonth = newVal
            }
        }
    }

    override fun onResume() {
        super.onResume()

        dialog?.window?.apply {
            attributes = attributes.apply {
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
            }
        }
    }

    private fun getMaxDayOfMonth(year: Int, month: Int): Int {
        return if (month == 2 && year % 4 == 0 && year % 100 != 0) 29 else dayOfMonthPreset[month - 1]
    }

    companion object {
        private val dayOfMonthPreset = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        private const val MIN_YEAR = 1900
        private const val MAX_YEAR = 3000

        private const val MIN_MONTH = 1
        private const val MAX_MONTH = 12

        private const val MIN_DATE = 1
        private const val MAX_DATE = 31
    }
}
