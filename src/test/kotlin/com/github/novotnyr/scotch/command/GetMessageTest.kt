package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.RabbitMqConnectionException
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.Test

class GetMessageTest {
    @Test
    fun testFailedConnection() = runBlocking {
        val configuration = RabbitConfiguration()
        configuration.user = ("guest")
        configuration.password = ("guest")
        configuration.virtualHost = ("/")

        try {
            GetMessage("cabbage", configuration).run()
            Assert.fail("No exception has been thrown despite wrong port")
        } catch (e: Exception) {
            // we cannot `assertThrows` with `suspend` functions
            // due to https://github.com/junit-team/junit5/issues/1851
            if (e !is RabbitMqConnectionException) {
                Assert.fail("Illegal exception thrown")
                e.printStackTrace()
            }
        }
    }

    @Test
    fun successfulConnection() = runBlocking {
        val configuration = RabbitConfiguration()
        configuration.user = ("guest")
        configuration.password = ("guest")
        configuration.virtualHost = ("/")
        configuration.port = 15672

        val messages = GetMessage("cabbage", configuration).run()
        print(messages)
    }
}