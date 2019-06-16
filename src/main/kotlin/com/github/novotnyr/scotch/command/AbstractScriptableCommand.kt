package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration

abstract class AbstractScriptableCommand<O>(rabbitConfiguration: RabbitConfiguration) :
    AbstractRestCommand<O>(rabbitConfiguration) {
    var description = ""
}
