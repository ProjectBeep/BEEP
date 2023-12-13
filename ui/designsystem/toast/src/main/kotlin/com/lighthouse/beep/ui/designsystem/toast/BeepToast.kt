package com.lighthouse.beep.ui.designsystem.toast

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.StringRes
import com.lighthouse.beep.core.common.exts.dp
import com.lighthouse.beep.core.ui.exts.preventTouchPropagation
import com.lighthouse.beep.ui.designsystem.toast.databinding.ToastDefaultBinding
import java.lang.ref.WeakReference

class BeepToast(context: Context) : Toast(context) {

    private val binding = ToastDefaultBinding.inflate(LayoutInflater.from(context))

    init {
        view = binding.root
        binding.root.preventTouchPropagation()
        setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 16.dp)
    }

    override fun setText(resId: Int) {
        setText(view?.context?.getString(resId))
    }

    override fun setText(s: CharSequence?) {
        binding.text.text = s
    }

    companion object {
        private var toastRef = WeakReference<BeepToast>(null)

        fun show(context: Context, s: CharSequence?, duration: Int = LENGTH_SHORT) {
            toastRef.get()?.cancel()
            BeepToast(context).apply {
                setText(s)
                setDuration(duration)
            }.also {
                toastRef = WeakReference(it)
                it.show()
            }
        }

        fun show(context: Context, @StringRes resId: Int, duration: Int = LENGTH_SHORT) {
            show(context, context.getString(resId), duration)
        }
    }
}