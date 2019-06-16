package com.github.novotnyr.scotch

import com.rabbitmq.client.ConnectionFactory.*

data class RabbitConfiguration(
    var host: String = DEFAULT_HOST,
    var port: Int = DEFAULT_AMQP_PORT,
    var virtualHost: String = "/",
    var user: String = DEFAULT_USER,
    var password: String = DEFAULT_PASS,
    var protocol: Protocol = Protocol.HTTP,
    var isAllowingInsecureTls: Boolean = false
) {

    override fun toString(): String {
        val safePassword = password.replace(".".toRegex(), "*")
        return "Rabbit Configuration: " +
                "host: $host, " +
                "port: $port, " +
                "virtual host: $virtualHost, " +
                "user:$user, " +
                "password:${safePassword}"
    }

    enum class Protocol {
        HTTP,
        HTTPS,
        RABBITMQ
    }

    companion object {
        const val DEFAULT_HTTP_PORT = 15672

        const val DEFAULT_HTTPS_PORT = 15671
    }
}
