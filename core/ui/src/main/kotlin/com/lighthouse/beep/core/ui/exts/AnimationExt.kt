package com.lighthouse.beep.core.ui.exts

import android.animation.Animator
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.setListener(
    doOnStart:(Animator)-> Unit = {},
    doOnEnd:(Animator)-> Unit = {},
    doOnCancel:(Animator)-> Unit = {},
    doOnRepeat:(Animator)-> Unit = {},
) = apply {
    setListener(object: Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            doOnStart(animation)
        }

        override fun onAnimationEnd(animation: Animator) {
            doOnEnd(animation)
        }

        override fun onAnimationCancel(animation: Animator) {
            doOnCancel(animation)
        }

        override fun onAnimationRepeat(animation: Animator) {
            doOnRepeat(animation)
        }
    })
}