package com.github.novotnyr.scotch.domain

import com.google.gson.annotations.SerializedName
import java.util.*

class RetrievedMessage {
    @SerializedName("payload_bytes")
    var payloadSize: String? = null

    var isRedelivered: Boolean = false

    var exchange: String? = null

    @SerializedName("message_count")
    var messagesLeftInQueue: String? = null

    var properties: Properties = Properties()

    var payload: String? = null

    override fun toString(): String {
        return "RetrievedMessage(" +
                "payloadSize=$payloadSize, " +
                "isRedelivered=$isRedelivered, " +
                "exchange=$exchange, " +
                "messagesLeftInQueue=$messagesLeftInQueue, " +
                "properties=$properties, payload=$payload)"
    }

    class Properties {
        @SerializedName("delivery_mode")
        var deliveryMode: Int = 0

        var headers: Map<String, String> = LinkedHashMap()
    }

}
