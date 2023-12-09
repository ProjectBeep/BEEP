package com.lighthouse.beep.ui.dialog.bottomsheet

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.Window
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.DialogFragment
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.lighthouse.beep.core.ui.animation.SimpleAnimatorListener
import com.lighthouse.beep.core.ui.animation.SimpleTransitionListener
import com.lighthouse.beep.core.ui.exts.viewHeight
import com.lighthouse.beep.theme.R
import com.lighthouse.beep.ui.dialog.bottomsheet.databinding.DialogBeepBottomSheetBinding

abstract class BeepBottomSheetDialog : DialogFragment() {

    private var _binding: DialogBeepBottomSheetBinding? = null

    private val binding: DialogBeepBottomSheetBinding
        get() = requireNotNull(_binding)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                requestFeature(Window.FEATURE_NO_TITLE)
                setCanceledOnTouchOutside(false)
                setDimAmount(0.3f)
                setOnShowListener {
                    showDialog()
                }
                setOnKeyListener { _, keyCode, _ ->
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        hideDialog()
                        true
                    } else {
                        false
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBeepBottomSheetBinding.inflate(inflater, container, false)
        val contentView = onCreateContentView(inflater, binding.containerContent, savedInstanceState)
        binding.containerContent.addView(contentView)
        binding.viewCancel.setOnClickListener {
            hideDialog()
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        viewHandle = null
        super.onDestroyView()
    }

    abstract fun onCreateContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View

    override fun onDismiss(dialog: DialogInterface) {
        hideDialog()
        super.onDismiss(dialog)
    }

    private var viewHandle: View? = null

    fun attachHandle(handle: View) {
        viewHandle = handle
        handle.setOnTouchListener(object : View.OnTouchListener {
            private var touchStartY = 0f
            private val maxTransitionY by lazy {
                binding.containerContent.viewHeight.toFloat()
            }

            private var animator: ViewPropertyAnimator? = null

            @SuppressLint("ClickableViewAccessibility")
            override fun onTouch(view: View, event: MotionEvent): Boolean {
                if (!view.isEnabled) {
                    return false
                }

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        touchStartY = event.rawY
                    }

                    MotionEvent.ACTION_MOVE -> {
                        binding.containerContent.translationY =
                            minOf(maxOf(event.rawY - touchStartY, 0f), maxTransitionY)
                    }

                    MotionEvent.ACTION_UP,
                    MotionEvent.ACTION_CANCEL -> {
                        val isShow =
                            binding.containerContent.translationY < binding.containerContent.height * 0.3
                        if (isShow) {
                            startAnimation()
                        } else {
                            hideDialog()
                        }
                    }
                }
                return true
            }

            private fun startAnimation() {
                val start = binding.containerContent.translationY
                val end = 0f
                animator = binding.containerContent.animate()
                    .setDuration(180L)
                    .setListener(object : SimpleAnimatorListener() {
                        override fun onAnimationStart(animator: Animator) {
                            viewHandle?.isEnabled = false
                        }

                        override fun onAnimationEnd(animator: Animator) {
                            viewHandle?.isEnabled = true
                        }

                        override fun onAnimationCancel(animator: Animator) {
                            viewHandle?.isEnabled = true
                        }
                    })
                    .setUpdateListener {
                        if (isAdded) {
                            binding.containerContent.translationY =
                                start - (start - end) * it.animatedFraction
                        }
                    }.also {
                        it.start()
                    }
            }
        })
    }

    private fun showDialog() {
        ConstraintSet().apply {
            clone(binding.root)
            connect(
                binding.containerContent.id,
                ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            clear(binding.containerContent.id, ConstraintSet.TOP)
        }.applyTo(binding.root)

        val trans = ChangeBounds().apply {
            duration = 180L
            interpolator = AccelerateInterpolator()
        }
        TransitionManager.beginDelayedTransition(binding.root, trans)
    }

    protected fun hideDialog() {
        ConstraintSet().apply {
            clone(binding.root)
            connect(
                binding.containerContent.id,
                ConstraintSet.TOP,
                ConstraintSet.PARENT_ID,
                ConstraintSet.BOTTOM
            )
            clear(binding.containerContent.id, ConstraintSet.BOTTOM)
        }.applyTo(binding.root)

        val trans = ChangeBounds().apply {
            duration = 180L
            interpolator = AccelerateInterpolator()
            addListener(object : SimpleTransitionListener() {
                override fun onTransitionStart(transition: Transition) {
                    viewHandle?.isEnabled = false
                }

                override fun onTransitionCancel(transition: Transition) {
                    dismiss()
                }

                override fun onTransitionEnd(transition: Transition) {
                    dismiss()
                }
            })
        }
        TransitionManager.beginDelayedTransition(binding.root, trans)
    }
}