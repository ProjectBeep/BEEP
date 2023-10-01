package com.lighthouse.beep.ui.dialog.textinput

import android.text.InputFilter
import android.text.InputType
import android.widget.EditText
import com.lighthouse.beep.core.common.exts.toDigit
import com.lighthouse.beep.ui.dialog.textinput.filter.DigitFilterWithMaxLength
import java.text.DecimalFormat

enum class TextInputFormat(
    val separator: Char = Char.MIN_VALUE,
    val filters: Array<InputFilter> = arrayOf(),
    val rawInputType: Int = InputType.TYPE_CLASS_TEXT,
    val inputType: Int = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE,
) {
    TEXT,
    BALANCE(
        separator = ',',
        filters = arrayOf(DigitFilterWithMaxLength(7)),
        rawInputType = InputType.TYPE_CLASS_NUMBER,
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
    ) {
        private val balanceFormat = DecimalFormat("###,###,###")

        override fun valueToTransformed(value: String): String {
            return balanceFormat.format(value.toDigit())
        }

        override fun transformedToValue(text: String): String {
            return text.filter { it.isDigit() }.toDigit().toString()
        }
    },
    BARCODE(
        separator = ' ',
        filters = arrayOf(DigitFilterWithMaxLength(24)),
        rawInputType = InputType.TYPE_CLASS_NUMBER,
        inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL,
    ) {
        override fun valueToTransformed(value: String): String {
            return value.chunked(4).joinToString(separator.toString())
        }

        override fun transformedToValue(text: String): String {
            return text.filter { it.isDigit() }
        }
    };

    open fun valueToTransformed(value: String): String {
        return value
    }

    open fun transformedToValue(text: String): String {
        return text
    }
}

fun EditText.applyFormat(format: TextInputFormat) {
    filters = format.filters
    inputType = format.inputType
    setRawInputType(format.rawInputType)
}