package com.github.novotnyr.scotch.command.api

class GetMessageRequest {
    var count = 1

    var ackMode = AckMode.ACK

    var responseEncoding = Encoding.AUTO

    var payloadSizeLimit: Long = -1

    enum class AckMode(val code: String) {
        ACK_REQUEUE("ack_requeue_true"),
        REJECT_REQUEUE("reject_requeue"),
        ACK("ack_requeue_false"),
        REJECT("reject_requeue_false");

        override fun toString(): String = code
    }

    enum class Encoding private constructor(val code: String) {
        AUTO("auto"),
        BASE64("base64");

        override fun toString(): String = code
    }
}
