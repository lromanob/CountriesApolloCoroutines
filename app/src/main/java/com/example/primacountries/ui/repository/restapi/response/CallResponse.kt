package it.rcs.restapi.data.source.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CallResponse<T>(
    @Json(name = "status")
    var status: String? = null, // OK
    @Json(name = "result")
    var result: Result<T>? = null
)