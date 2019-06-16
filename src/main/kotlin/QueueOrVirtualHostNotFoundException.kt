package com.github.novotnyr.scotch

class QueueOrVirtualHostNotFoundException(
    val virtualHost: String,
    val queue: String,
    val responseBodyString: String
) : RuntimeException("Queue ($queue) or virtual host ($virtualHost) not found")