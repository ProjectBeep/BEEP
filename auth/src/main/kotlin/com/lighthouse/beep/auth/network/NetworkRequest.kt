package com.lighthouse.beep.auth.network

internal data class NetworkRequest(
    val method: RequestMethod = RequestMethod.GET,
    val url: String,
    val params: Map<String, String> = emptyMap(),
    val headers: Map<String, String> = emptyMap(),
)

internal enum class RequestMethod {
    GET, POST
}
