package com.lighthouse.beep.library.textformat

import android.text.InputFilter
import android.text.Selection
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.lighthouse.beep.core.ui.text.SimpleTextWatcher
import kotlin.math.max
import kotlin.math.min

fun EditText.setInputFormat(
    inputFormat: TextInputFormat,
    inputFilters: Array<InputFilter> = inputFormat.filters,
    onValueChanged: (value: String) -> Unit,
) {
    filters = inputFilters
    inputType = inputFormat.inputType
    setRawInputType(inputFormat.rawInputType)
    if (inputFormat == TextInputFormat.TEXT) {
        addTextChangedListener {
            onValueChanged(it.toString())
        }
    } else {
        var displayText = text.toString()
        addTextChangedListener(object: SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val newString = s.toString()
                val oldDisplayText = displayText
                if (oldDisplayText == newString) {
                    return
                }
                val newValue = if (
                    before == 1 && count == 0 &&
                    start < oldDisplayText.length &&
                    oldDisplayText[start] == inputFormat.separator
                ) {
                    val prefix = newString.substring(0, Integer.max(start - 1, 0))
                    val text = newString.substring(Integer.max(start, 0), newString.length)
                    inputFormat.transformedToValue(prefix + text)
                } else {
                    inputFormat.transformedToValue(newString)
                }
                displayText = inputFormat.valueToTransformed(newValue)
                onValueChanged(newValue)

                val newDisplayText = displayText
                val selection = if (oldDisplayText.length == start + before) {
                    newDisplayText.length
                } else {
                    val separator = inputFormat.separator
                    val endStringCount = max(oldDisplayText.length - start - before, 0)
                    val oldSeparatorCount = oldDisplayText
                        .substring(start + before, oldDisplayText.length)
                        .count { it == separator }
                    val endNumCount = max(endStringCount - oldSeparatorCount, 0)
                    var index = 0
                    var numCount = 0
                    while (
                        newDisplayText.lastIndex - index >= 0 &&
                        (numCount < endNumCount || newDisplayText[newDisplayText.lastIndex - index] == separator)
                    ) {
                        if (newDisplayText[newDisplayText.lastIndex - index] != separator) {
                            numCount += 1
                        }
                        index += 1
                    }
                    newDisplayText.lastIndex - index + 1
                }
                setText(newDisplayText, selection)
            }

            private fun setText(displayText: String, selection: Int) {
                setText(displayText)
                Selection.setSelection(text, min(selection, displayText.length))
            }
        })
    }
}