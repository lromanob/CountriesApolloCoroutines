package it.rcs.restapi.data.source.api.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result<T>(
    @Json(name = "code")
    var code: String? = null, // ARCHIVE_OK
    @Json(name = "msg")
    var msg: String? = null, // Archive is available
    @Json(name = "data")
    var `data`: T? = null
)