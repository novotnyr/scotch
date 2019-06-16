package com.github.novotnyr.scotch.command.script

import com.github.novotnyr.scotch.command.Command

interface ScriptOutputSerializer<in C : Command<O>, O> {
    fun serialize(command: C, output: O)
}
