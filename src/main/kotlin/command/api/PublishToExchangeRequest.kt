package com.github.novotnyr.scotch.command.api

import com.google.gson.annotations.SerializedName

class PublishToExchangeRequest(
    @SerializedName("routing_key") var routingKey: String? = null,
    @SerializedName("payload") var base64Payload: String? = null,
    @SerializedName("payload_encoding") val payloadEncoding: String = "base64",
    var properties: Properties = Properties()
) {

    var contentType: String? = null
        set(contentType) {
            field = contentType?.also { properties = properties.copy(contentType = it) }
        }

    var replyTo: String? = null
        set(replyTo) {
            field = replyTo?.also { properties = properties.copy(replyTo = it) }
        }

    var headers: Map<String, String> = emptyMap()
        set(headers) {
            field = headers.also { properties = properties.copy(headers = it) }
        }

    data class Properties(
        @SerializedName("content_type") val contentType: String? = null,
        @SerializedName("reply_to") val replyTo: String? = null,
        val headers: Map<String, String> = emptyMap()
    )
}
