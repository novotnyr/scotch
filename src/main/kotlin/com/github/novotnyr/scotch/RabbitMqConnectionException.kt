package com.github.novotnyr.scotch

class RabbitMqConnectionException: RabbitMqAdminException {
    constructor(message: String, cause: Throwable, rabbitConfiguration: RabbitConfiguration) : super(message, cause) {
        this.rabbitConfiguration = rabbitConfiguration;
    }

    constructor(rabbitConfiguration: RabbitConfiguration, cause: Throwable) :
            this("Unable to connect to RabbitMQ broker on "
                    + rabbitConfiguration.host + ":" + rabbitConfiguration.port
                    + " via " + rabbitConfiguration.protocol, cause, rabbitConfiguration)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
}