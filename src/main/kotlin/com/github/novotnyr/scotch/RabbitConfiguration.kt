package com.github.novotnyr.scotch

import com.rabbitmq.client.ConnectionFactory.*

class RabbitConfiguration {
    var host = DEFAULT_HOST

    var port = DEFAULT_AMQP_PORT

    var virtualHost = "/"

    var user = DEFAULT_USER

    var password = DEFAULT_PASS

    var protocol = Protocol.HTTP

    var isAllowingInsecureTls: Boolean = false

    override fun toString(): String {
        val sb = StringBuilder("Rabbit Configuration: ")
        sb.append("host: ").append(host)
        sb.append(", port: ").append(port)
        sb.append(", virtualHost: ").append(virtualHost)
        sb.append(", user: ").append(user)
        sb.append(", password: ").append(password.replace(".".toRegex(), "*"))
        return sb.toString()
    }

    enum class Protocol {
        HTTP,
        HTTPS,
        RABBITMQ
    }

    companion object {
        val DEFAULT_HTTP_PORT = 15672

        val DEFAULT_HTTPS_PORT = 15671
    }
}
