package com.github.novotnyr.scotch.command.script

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.command.GetMessage
import com.github.novotnyr.scotch.command.ListExchanges
import com.github.novotnyr.scotch.domain.RetrievedMessage
import domain.Exchange
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicReference


class ExecuteScriptTest {
    @Test
    fun test() = runBlocking {
        val executeScript = ExecuteScript(scriptFile = "src/test/resources/example.get.rabbitmq")
        executeScript.setOutputSerializer(GetMessage::class.java, object : ScriptOutputSerializer<GetMessage, List<RetrievedMessage>> {
            override fun serialize(command: GetMessage, output: List<RetrievedMessage>) {
                for (message in output) {
                    println(message)
                }
            }
        })
        executeScript.run()
    }

    @Test
    fun testImplicitConfig() = runBlocking {
        val executeScript = ExecuteScript(scriptFile = "src/test/resources/example.no-config.rabbitmq")
        executeScript.setOutputSerializer(GetMessage::class.java, object : ScriptOutputSerializer<GetMessage, List<RetrievedMessage>> {
            override fun serialize(command: GetMessage, output: List<RetrievedMessage>) {
                for (message in output) {
                    println(message)
                }
            }
        })
        executeScript.run()
    }

    @Test
    fun testImplicitConfigWithExplicitConfiguration() = runBlocking {
        val rabbitConfiguration = RabbitConfiguration()
        val executeScript = ExecuteScript(rabbitConfiguration, scriptFile = "src/test/resources/example.no-config.rabbitmq")
        executeScript.setOutputSerializer(GetMessage::class.java, object : ScriptOutputSerializer<GetMessage, List<RetrievedMessage>> {
            override fun serialize(command: GetMessage, output: List<RetrievedMessage>) {
                for (message in output) {
                    println(message)
                }
            }
        })
        executeScript.run()
    }

    @Test
    fun testListExchangesWithExplicitConfiguration() = runBlocking {
        val rabbitConfiguration = RabbitConfiguration()
        val executeScript = ExecuteScript(rabbitConfiguration, scriptFile = "src/test/resources/example.exchanges.rabbitmq")
        executeScript.includedCommandIndices = listOf(0)
        val exchanges = AtomicReference<List<Exchange>>()
        executeScript.setOutputSerializer(ListExchanges::class.java, object : ScriptOutputSerializer<ListExchanges, List<Exchange>> {
            override fun serialize(command: ListExchanges, output: List<Exchange>) {
                exchanges.set(output)
            }
        })
        executeScript.run()
        assertTrue(exchanges.get().isNotEmpty())
    }

    @Test
    fun testListExchangesWithVhostAndExplicitConfiguration() = runBlocking {
        val rabbitConfiguration = RabbitConfiguration()
        val executeScript = ExecuteScript(rabbitConfiguration, scriptFile = "src/test/resources/example.exchanges.rabbitmq")
        executeScript.includedCommandIndices = listOf(1)
        val exchanges = AtomicReference<List<Exchange>>()
        executeScript.setOutputSerializer(ListExchanges::class.java, object : ScriptOutputSerializer<ListExchanges, List<Exchange>> {
            override fun serialize(command: ListExchanges, output: List<Exchange>) {
                exchanges.set(output)
            }
        })
        executeScript.run()
        assertTrue(exchanges.get().isNotEmpty())
    }


}