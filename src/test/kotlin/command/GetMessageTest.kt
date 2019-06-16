package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.RabbitMqConnectionException
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

class GetMessageTest {
    @Test
    fun testFailedConnection() = runBlocking {
        val configuration = RabbitConfiguration()

        try {
            GetMessage("cabbage", configuration).run()
            fail<Unit>("No exception has been thrown despite wrong port")
        } catch (e: Exception) {
            // we cannot `assertThrows` with `suspend` functions
            // due to https://github.com/junit-team/junit5/issues/1851
            if (e !is RabbitMqConnectionException) {
                fail<Unit>("Illegal exception thrown", e)
            }
        }
    }

    @Test
    fun successfulConnection() = runBlocking {
        val configuration = RabbitConfiguration()
        val messages = GetMessage("cabbage", configuration).run()
        print(messages)
    }
}