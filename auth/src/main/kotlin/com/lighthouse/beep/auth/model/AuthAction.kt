package com.lighthouse.beep.auth.model

internal enum class AuthAction {
    NONE,
    SIGN_IN,
    SIGN_OUT,
    WITHDRAWAL;

    companion object {
        fun of(name: String): AuthAction {
            return runCatching {
                entries.find { it.name == name } ?: NONE
            }.getOrDefault(NONE)
        }
    }
}