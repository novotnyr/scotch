package com.github.novotnyr.scotch

class ExchangeOrVirtualHostNotFoundException(
    val virtualHost: String, val exchange: String, val responseBodyString: String
) : RuntimeException("Exchange ($exchange) or virtual host ($virtualHost) not found")