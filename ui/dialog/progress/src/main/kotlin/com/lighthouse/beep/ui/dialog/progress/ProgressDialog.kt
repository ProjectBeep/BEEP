package com.lighthouse.beep.ui.dialog.progress

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment
import com.lighthouse.beep.theme.R as ThemeR

class ProgressDialog : DialogFragment(R.layout.dialog_progress) {

    companion object {
        const val TAG = "ProgressDialog"

        fun newInstance(param: ProgressParam = ProgressParam()): ProgressDialog {
            return ProgressDialog().apply {
                arguments = param.buildBundle()
            }
        }
    }

    private var onCancelListener: OnCancelListener? = null

    fun setOnCancelListener(listener: OnCancelListener) {
        onCancelListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, ThemeR.style.Theme_Dialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                val backgroundColor = ProgressParam.getBackgroundColor(arguments)
                setBackgroundDrawable(ColorDrawable(backgroundColor))
            }
            setOnKeyListener { _, keyCode, _ ->
                if(keyCode == KeyEvent.KEYCODE_BACK){
                    onCancelListener?.onCancel()
                    true
                } else {
                    false
                }
            }
        }
    }
}
