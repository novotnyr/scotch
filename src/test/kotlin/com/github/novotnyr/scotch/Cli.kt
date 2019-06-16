package com.github.novotnyr.scotch

import com.github.novotnyr.scotch.command.PublishToExchange
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking{
    val confirmation = PublishToExchange(rabbitConfiguration = RabbitConfiguration(port = RabbitConfiguration.DEFAULT_HTTP_PORT)).run {
        routingKey = "cabbage"
        encodeAndSetUtf8Contents("Hello")
        run()
    }
    println(if(confirmation.isRouted) "yes" else "no")
}