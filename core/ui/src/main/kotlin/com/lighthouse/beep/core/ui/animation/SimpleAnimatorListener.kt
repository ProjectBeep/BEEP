package com.lighthouse.beep.core.ui.animation

import android.animation.Animator

open class SimpleAnimatorListener : Animator.AnimatorListener {
    override fun onAnimationStart(animator: Animator) = Unit

    override fun onAnimationEnd(animator: Animator) = Unit

    override fun onAnimationCancel(animator: Animator) = Unit

    override fun onAnimationRepeat(animator: Animator) = Unit
}
