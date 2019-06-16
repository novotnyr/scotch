package com.github.novotnyr.scotch

class RabbitMqConnectionException : RabbitMqAdminException {
    constructor(message: String, cause: Throwable, rabbitConfiguration: RabbitConfiguration) : super(message, cause) {
        this.rabbitConfiguration = rabbitConfiguration
    }

    constructor(configuration: RabbitConfiguration, cause: Throwable) :
            this(
                "Unable to connect to RabbitMQ broker on ${configuration.host}:${configuration.port} " +
                        "via ${configuration.protocol}",
                cause, configuration
            )

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}