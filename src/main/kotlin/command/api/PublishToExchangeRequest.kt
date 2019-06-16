package com.github.novotnyr.scotch.command.api

import com.google.gson.annotations.SerializedName

class PublishToExchangeRequest(
    @SerializedName("routing_key") var routingKey: String? = null,
    @SerializedName("payload") var base64Payload: String? = null,
    @SerializedName("payload_encoding") val payloadEncoding: String = "base64",
    var properties: Properties = Properties()
) {

    var contentType: String? = null
        set(value) {
            value?.let { this.properties.copy(contentType = it) }
            field = value
        }

    var replyTo: String? = null
        set(value) {
            value?.let { this.properties.copy(replyTo = it) }
            field = value
        }

    var headers: Map<String, String> = emptyMap()
        set(value) {
            this.properties.copy(headers = this.properties.headers)
            field = value
        }

    data class Properties(
        @SerializedName("content_type") val contentType: String? = null,
        @SerializedName("reply_to") val replyTo: String? = null,
        val headers: Map<String, String> = emptyMap()
    )
}
