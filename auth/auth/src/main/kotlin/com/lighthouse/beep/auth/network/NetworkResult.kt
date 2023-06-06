package com.lighthouse.beep.auth.network

internal sealed class NetworkResult {

    data class Success(val response: String) : NetworkResult()

    data class Failed(val throwable: Throwable) : NetworkResult()
}
