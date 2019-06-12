package com.github.novotnyr.scotch.command.script

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.command.Command
import java.util.*

data class Script(val configuration: RabbitConfiguration? = null, var commands: MutableList<Command<*>> = ArrayList()) {
    fun append(command: Command<*>) {
        commands.add(command)
    }
}
