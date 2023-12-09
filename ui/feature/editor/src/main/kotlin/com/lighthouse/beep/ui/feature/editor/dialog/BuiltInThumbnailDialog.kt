package com.lighthouse.beep.ui.feature.editor.dialog

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
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
import com.lighthouse.beep.model.gifticon.GifticonBuiltInThumbnail
import com.lighthouse.beep.theme.R as ThemeR
import com.lighthouse.beep.ui.feature.editor.databinding.DialogBuiltInThumbnailBinding
import com.lighthouse.beep.ui.feature.editor.dialog.adapter.BuiltInThumbnailAdapter
import com.lighthouse.beep.ui.feature.editor.dialog.adapter.OnBuiltInThumbnailListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BuiltInThumbnailDialog : DialogFragment() {

    companion object {
        const val TAG = "BuiltInThumbnail"
    }

    private var _binding: DialogBuiltInThumbnailBinding? = null
    private val binding
        get() = requireNotNull(_binding)

    private val builtInThumbnailAdapter = BuiltInThumbnailAdapter(
        listener = object : OnBuiltInThumbnailListener {
            override fun isSelectedFlow(item: GifticonBuiltInThumbnail): Flow<Boolean> {
                return flow { emit(false) }
            }

            override fun onClick(item: GifticonBuiltInThumbnail) {

            }
        }
    )

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window?.apply {
                requestFeature(Window.FEATURE_NO_TITLE)
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
        setStyle(STYLE_NO_FRAME, ThemeR.style.Theme_Dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogBuiltInThumbnailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpBuiltInList()
        setOnClickEvent()
    }

    override fun onDismiss(dialog: DialogInterface) {
        hideDialog()
        super.onDismiss(dialog)
    }

    private fun setUpBuiltInList() {
        builtInThumbnailAdapter.submitList(GifticonBuiltInThumbnail.entries)
        binding.gridBuiltInThumbnail.adapter = builtInThumbnailAdapter
    }

    private fun setOnClickEvent() {
        binding.viewCancel.setOnClickListener {
            hideDialog()
        }

        binding.viewHandle.setOnTouchListener(object : OnTouchListener {
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
                        startAnimation()
                    }
                }
                return true
            }

            private fun startAnimation() {
                val isShow =
                    binding.containerContent.translationY < binding.containerContent.height * 0.3
                if (isShow) {
                    val start = binding.containerContent.translationY
                    val end = 0f
                    animator = binding.containerContent.animate()
                        .setListener(object : SimpleAnimatorListener() {
                            override fun onAnimationStart(animator: Animator) {
                                binding.viewHandle.isEnabled = false
                            }

                            override fun onAnimationEnd(animator: Animator) {
                                binding.viewHandle.isEnabled = true
                            }

                            override fun onAnimationCancel(animator: Animator) {
                                binding.viewHandle.isEnabled = true
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
                } else {
                    hideDialog()
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
            duration = 300L
            interpolator = AccelerateInterpolator()
        }
        TransitionManager.beginDelayedTransition(binding.root, trans)
    }

    private fun hideDialog() {
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
            duration = 300L
            interpolator = AccelerateInterpolator()
            addListener(object : SimpleTransitionListener() {
                override fun onTransitionStart(transition: Transition) {
                    binding.viewHandle.isEnabled = false
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