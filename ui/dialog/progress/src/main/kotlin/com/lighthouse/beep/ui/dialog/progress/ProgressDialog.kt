package com.lighthouse.beep.ui.dialog.progress

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.lighthouse.beep.ui.core.binding.viewBindings
import com.lighthouse.beep.ui.dialog.progress.databinding.DialogProgressBinding

class ProgressDialog : DialogFragment(R.layout.dialog_progress) {

    companion object {
        fun newInstance(params: ProgressParam = ProgressParam()): ProgressDialog {
            return ProgressDialog().apply {
                arguments = params.buildBundle()
            }
        }
    }

    private val binding by viewBindings<DialogProgressBinding>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setDimAmount(ProgressParam.getDimAmount(arguments))
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.setBackgroundColor(ProgressParam.getBackgroundColor(arguments))
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
}
