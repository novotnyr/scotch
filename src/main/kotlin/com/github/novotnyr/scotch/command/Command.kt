package com.github.novotnyr.scotch.command

import java.util.concurrent.CompletableFuture

interface Command<O> {
    fun run() : CompletableFuture<O>
}
