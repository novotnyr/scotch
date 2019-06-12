package com.github.novotnyr.scotch

class UnknownProtocolException : RuntimeException {
    constructor() : super() {}

    constructor(msg: String) : super(msg) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}

    constructor(cause: Throwable) : super(cause) {}
}