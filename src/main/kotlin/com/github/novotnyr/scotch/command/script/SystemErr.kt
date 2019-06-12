package com.github.novotnyr.scotch.command.script

class SystemErr : StdErr {
    override fun println(message: String) {
        System.err.println(message)
    }
}
