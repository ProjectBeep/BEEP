package com.lighthouse.beep.core.ui.animation

import androidx.transition.Transition
import androidx.transition.Transition.TransitionListener

open class SimpleTransitionListener : TransitionListener {

    override fun onTransitionStart(transition: Transition) = Unit

    override fun onTransitionEnd(transition: Transition) = Unit

    override fun onTransitionCancel(transition: Transition) = Unit

    override fun onTransitionPause(transition: Transition) = Unit

    override fun onTransitionResume(transition: Transition) = Unit
}