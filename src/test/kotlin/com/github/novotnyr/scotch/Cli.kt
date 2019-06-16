package com.github.novotnyr.scotch

import com.github.novotnyr.scotch.command.PublishToExchange
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking{
    val publishToExchange = PublishToExchange(rabbitConfiguration = RabbitConfiguration(port = RabbitConfiguration.DEFAULT_HTTP_PORT))
    publishToExchange.routingKey = "cabbage"
    publishToExchange.encodeAndSetUtf8Contents("Hello")

    val confirmation = publishToExchange.run()
    println(if(confirmation.isRouted) "yes" else "no")
}