package com.github.novotnyr.scotch.command

interface Command<O> {
    suspend fun run() : O
}
