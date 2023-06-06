package com.lighthouse.beep.library.network

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

open class NetworkTask(
    private val client: OkHttpClient,
) {

    suspend fun requestApi(request: NetworkRequest): NetworkResult {
        return runCatching {
            requestApi(request.buildRequestApi())
        }.getOrElse { e ->
            NetworkResult.Failed(e)
        }
    }

    @Throws(Exception::class)
    private suspend fun requestApi(request: Request): NetworkResult {
        val call = client.newCall(request)
        return suspendCancellableCoroutine { continuation ->
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    if (continuation.isCancelled) return
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        continuation.resume(
                            NetworkResult.Success(
                                response.body?.string().orEmpty()
                            )
                        )
                    } else {
                        continuation.resumeWithException(IllegalStateException())
                    }
                }
            })

            continuation.invokeOnCancellation {
                try {
                    call.cancel()
                } catch (e: Throwable) {
                    continuation.resumeWithException(e)
                }
            }
        }
    }
}