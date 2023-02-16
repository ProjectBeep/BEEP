package com.lighthouse.features.common.dialog.originimage

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import coil.load
import com.lighthouse.core.android.exts.getRequiredParcelableExtra
import com.lighthouse.features.common.Extras
import com.lighthouse.features.common.R
import com.lighthouse.features.common.binding.viewBindings
import com.lighthouse.features.common.databinding.DialogOriginImageBinding

class OriginImageDialog : DialogFragment(R.layout.dialog_origin_image) {

    private val binding: DialogOriginImageBinding by viewBindings()

    private val originUri
        get() = arguments?.getRequiredParcelableExtra<Uri>(Extras.KEY_ORIGIN_IMAGE)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
        }

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
