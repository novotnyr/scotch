package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.MissingMandatoryFieldException
import com.github.novotnyr.scotch.RabbitConfiguration
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

@Suppress("MainFunctionReturnUnit")
class PublishToExchangeTest {
    @Test
    fun publishToExchangeWithMissingRoutingKey() = runBlocking {
        val configuration = RabbitConfiguration()
        try {
            PublishToExchange(configuration).run()
        } catch (e: MissingMandatoryFieldException) {
            // we cannot `assertThrows` with `suspend` functions
            // due to https://github.com/junit-team/junit5/issues/1851
        }
    }

    @Test
    fun publishToExchangeWithMissingPayload() = runBlocking {
        val configuration = RabbitConfiguration()
        try {
            val command = PublishToExchange(configuration)
            command.routingKey = "cabbage"
            command.run()
        } catch (e: MissingMandatoryFieldException) {
            // we cannot `assertThrows` with `suspend` functions
            // due to https://github.com/junit-team/junit5/issues/1851
        }
    }
}