package com.lighthouse.beep.auth.extension

import com.lighthouse.beep.auth.network.NetworkResult
import org.json.JSONObject

internal fun NetworkResult.mapJson(): Result<JSONObject> =
    safeApi(this) { response ->
        JSONObject(response)
    }

internal fun <T : Any> safeApi(result: NetworkResult, convert: (String) -> T): Result<T> {
    return when (result) {
        is NetworkResult.Success -> {
            runCatching {
                Result.success(convert(result.response))
            }.getOrElse { e ->
                Result.failure(e)
            }
        }

        is NetworkResult.Failed -> Result.failure(result.throwable)
    }
}
