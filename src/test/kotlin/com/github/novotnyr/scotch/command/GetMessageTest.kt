package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import org.awaitility.Awaitility.await
import org.junit.Assert
import org.junit.jupiter.api.Test
import java.net.ConnectException

class GetMessageTest {
    @Test
    fun testFailedConnection() {
        val configuration = RabbitConfiguration()
        configuration.user = ("guest")
        configuration.password = ("guest")
        configuration.virtualHost = ("/")

        val command = GetMessage("cabbage", configuration)
        val messagesFuture = command.run()

        await().until(messagesFuture::isDone);

        messagesFuture.handle { result, exception -> {
            Assert.assertNotNull(exception)
            Assert.assertTrue(exception is ConnectException)
        }}

    }

    @Test
    fun successfulConnection() {
        val configuration = RabbitConfiguration()
        configuration.user = ("guest")
        configuration.password = ("guest")
        configuration.virtualHost = ("/")
        configuration.port = 15672

        val command = GetMessage("cabbage", configuration)
        val messagesFuture = command.run()
        await().until(messagesFuture::isDone);
        val now = messagesFuture.get()
        print(now)
    }
}