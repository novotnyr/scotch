package com.github.novotnyr.scotch.command.script

import com.github.novotnyr.scotch.command.Command

class StdErrOutputSerializer<O>(private val stdErr: StdErr) : ScriptOutputSerializer<Command<O>, O> {
    override fun serialize(command: Command<O>, output: O) {
        stdErr.println(output.toString())
    }
}
