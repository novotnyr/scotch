package com.github.novotnyr.scotch.command.script

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.UnknownProtocolException
import com.rabbitmq.client.ConnectionFactory


class RabbitConfigurationParser {
    fun isConfiguration(configurationMap: Map<String, Any>): Boolean {
        return configurationMap.containsKey("host")
    }

    fun parseConfiguration(configurationMap: Map<String, Any>): RabbitConfiguration {
        val configuration = RabbitConfiguration()
        configuration.protocol = parseProtocol(configurationMap)
        deducePortFromProtocol(configuration, configurationMap)
        parseValue(configurationMap, "host")?.let { configuration.host = it }
        parseValue(configurationMap, "user")?.let { configuration.user = it }
        parseValue(configurationMap, "password")?.let { configuration.password = it }
        parseValue(configurationMap, "vhost")?.let { configuration.virtualHost = it }

        return configuration
    }

    private fun parseValue(configurationMap: Map<String, Any>, key: String): String? {
        val value = configurationMap[key]
        return value as? String
    }

    private fun deducePortFromProtocol(configuration: RabbitConfiguration, configurationMap: Map<String, Any>) {
        val port = configurationMap["port"]
        if (port is Int) {
            configuration.port = port
            return
        } else {
            when (configuration.protocol) {
                RabbitConfiguration.Protocol.HTTP -> configuration.port = RabbitConfiguration.DEFAULT_HTTP_PORT
                RabbitConfiguration.Protocol.HTTPS -> configuration.port = RabbitConfiguration.DEFAULT_HTTPS_PORT
                RabbitConfiguration.Protocol.RABBITMQ -> configuration.port = ConnectionFactory.DEFAULT_AMQP_PORT
            }
        }
    }

    private fun parseProtocol(configurationMap: Map<String, Any>): RabbitConfiguration.Protocol {
        val protocol = parseValue(configurationMap, "protocol") ?: RabbitConfiguration.Protocol.HTTP.name
        try {
            return RabbitConfiguration.Protocol.valueOf(protocol.toUpperCase())
        } catch (e: IllegalArgumentException) {
            throw UnknownProtocolException("Unsupported protocol $protocol")
        }

    }
}
