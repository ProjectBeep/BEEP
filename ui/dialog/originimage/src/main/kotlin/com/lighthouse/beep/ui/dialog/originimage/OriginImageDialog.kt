package com.lighthouse.beep.ui.dialog.originimage

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import coil.load
import com.lighthouse.beep.ui.core.binding.viewBindings
import com.lighthouse.beep.ui.dialog.originimage.databinding.DialogOriginImageBinding

class OriginImageDialog : DialogFragment(R.layout.dialog_origin_image) {

    companion object {
        fun newInstance(params: OriginImageParams): OriginImageDialog {
            return OriginImageDialog().apply {
                arguments = params.buildBundle()
            }
        }
    }

    private val binding by viewBindings<DialogOriginImageBinding>()

    private val originUri
        get() = OriginImageParams.getUri(arguments)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setDimAmount(0f)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.root.setOnClickListener {
            dismiss()
        }

        binding.ivOrigin.load(originUri)
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
