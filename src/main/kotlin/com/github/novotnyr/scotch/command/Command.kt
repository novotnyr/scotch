package com.github.novotnyr.scotch.command

interface Command<out O> {
    suspend fun run() : O
}
