package com.lighthouse.features.home.model

internal sealed class HomeAction {

    object None : HomeAction()

    object MapOpen : HomeAction()

    object ExpiredListOpen : HomeAction()
}
