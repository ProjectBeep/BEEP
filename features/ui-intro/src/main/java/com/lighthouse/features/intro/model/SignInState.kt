package com.lighthouse.features.intro.model

internal sealed class SignInState {

    object None : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
    data class Failed(val e: Exception) : SignInState() {

        constructor(throwable: Throwable) : this(throwable as Exception)
    }
}
