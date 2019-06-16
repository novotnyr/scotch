package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.http.HttpClientFactory

abstract class AbstractScriptableCommand<O> : AbstractRestCommand<O> {
    constructor(rabbitConfiguration: RabbitConfiguration, httpClientFactory: HttpClientFactory) : super(
        rabbitConfiguration,
        httpClientFactory
    )

    constructor(rabbitConfiguration: RabbitConfiguration) : super(rabbitConfiguration)

    var description = ""
}
