package com.github.novotnyr.scotch.command.api

import com.google.gson.annotations.SerializedName

class PublishToExchangeRequest {
    @SerializedName("routing_key")
    var routingKey: String? = null

    @SerializedName("payload")
    var base64Payload: String? = null

    @SerializedName("payload_encoding")
    val payloadEncoding = "base64"

    var properties = Properties()

    var contentType: String? = null
        set(value) {
            value?.let { this.properties.contentType = it }
            field = value
        }

    var replyTo: String? = null
        set(value) {
            value?.let { this.properties.replyTo = it }
            field = value
        }

    var headers : Map<String, String> = LinkedHashMap()
        set(value) {
            this.properties.headers = value
            field = value
        }

    class Properties {
        @SerializedName("content_type")
        var contentType: String? = null

        @SerializedName("reply_to")
        var replyTo: String? = null

        var headers: Map<String, String> = LinkedHashMap()
    }
}
