package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.MissingMandatoryFieldException
import com.github.novotnyr.scotch.RabbitConfiguration
import org.awaitility.Awaitility.await
import org.junit.Assert.assertTrue
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class PublishToExchangeTest {
    @Test
    fun publishToExchangeWithMissingRoutingKey() {
        val configuration = RabbitConfiguration()
        configuration.user = ("guest")
        configuration.password = ("guest")
        configuration.virtualHost = ("/")
        configuration.port = 15672

        val command = PublishToExchange(configuration)
        val future = command.run()

        assertThrows(MissingMandatoryFieldException::class.java) {
            await().until(future::isDone);

            val get = future.get()
            assertTrue(get.isRouted)
        }
    }

    @Test
    fun publishToExchangeWithMissingPayload() {
        val configuration = RabbitConfiguration()
        configuration.user = ("guest")
        configuration.password = ("guest")
        configuration.virtualHost = ("/")
        configuration.port = 15672

        val command = PublishToExchange(configuration)
        command.routingKey = "cabbage"
        val future = command.run()


        assertThrows(MissingMandatoryFieldException::class.java) {
            await().until(future::isDone);
            val get = future.get()
            assertTrue(get.isRouted)
        }
    }
}