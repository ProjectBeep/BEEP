package com.lighthouse.beep.auth.extension

import android.net.Uri
import com.lighthouse.beep.auth.network.NetworkRequest
import com.lighthouse.beep.auth.network.RequestMethod
import okhttp3.FormBody
import okhttp3.Request

internal fun NetworkRequest.buildRequestApi(): Request {
    return Request.Builder().also { builder ->
        for ((key, value) in headers) {
            builder.addHeader(key, value)
        }

        when (method) {
            RequestMethod.GET -> {
                builder.url(url.buildUri(params).toString())
            }

            RequestMethod.POST -> {
                builder.post(params.buildFormBody())
                builder.url(url)
            }
        }
    }.build()
}

internal fun String.buildUri(queries: Map<String, String> = emptyMap()): Uri {
    return Uri.Builder().also { builder ->
        builder.encodedPath(this)
        for ((key, value) in queries) {
            builder.appendQueryParameter(key, value)
        }
    }.build()
}

internal fun Map<String, String>.buildFormBody(): FormBody {
    return FormBody.Builder().also { builder ->
        for ((key, value) in this) {
            builder.add(key, value)
        }
    }.build()
}
