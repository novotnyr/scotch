package com.github.novotnyr.scotch.domain

import com.google.gson.annotations.SerializedName

data class RetrievedMessage(
    @SerializedName("payload_bytes")
    val payloadSize: String? = null,
    val isRedelivered: Boolean = false,
    val exchange: String = "",
    @SerializedName("message_count")
    val messagesLeftInQueue: String? = null,
    val properties: Properties = Properties(),
    val payload: String? = null
) {

    override fun toString(): String {
        val exchange = if(exchange.isNotEmpty()) exchange else "amq.default"

        return "RetrievedMessage(" +
                "payloadSize=$payloadSize, " +
                "isRedelivered=$isRedelivered, " +
                "exchange=$exchange, " +
                "messagesLeftInQueue=$messagesLeftInQueue, " +
                "properties=$properties, " +
                "payload=$payload)"
    }

    data class Properties(
        @SerializedName("delivery_mode") val deliveryMode: Int = 0,
        val headers: Map<String, String> = emptyMap()
    )
}
