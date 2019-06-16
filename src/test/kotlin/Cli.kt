package com.github.novotnyr.scotch

import com.github.novotnyr.scotch.command.GetMessage
import com.github.novotnyr.scotch.command.PublishToExchange
import com.github.novotnyr.scotch.http.SingletonHttpClientFactory
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>): Unit = runBlocking{
    val configuration = RabbitConfiguration(port = RabbitConfiguration.DEFAULT_HTTP_PORT)
    val clientFactory = SingletonHttpClientFactory()

    if (args[0] == "publish") {
        val confirmation = PublishToExchange(configuration).run {
            httpClientFactory = clientFactory
            routingKey = "cabbage"
            headers = mapOf("id" to "1")
            encodeAndSetUtf8Contents("Hello")
            run()
        }
        println(if(confirmation.isRouted) "yes" else "no")
    } else if (args[0] == "get") {
        val config = RabbitConfiguration(port = RabbitConfiguration.DEFAULT_HTTP_PORT)
        val messages = GetMessage("reply", config).run()
        println(messages)
    }
    clientFactory.getClient(configuration).dispatcher().executorService().shutdown()
}