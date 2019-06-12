package com.github.novotnyr.scotch.command.script

import com.github.novotnyr.scotch.command.GetMessage
import com.github.novotnyr.scotch.domain.RetrievedMessage
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test


class ExecuteScriptTest {
    @Test
    fun test() = runBlocking {
        val executeScript = ExecuteScript(null)
        executeScript.scriptFile = "src/test/resources/example.get.rabbitmq"
        executeScript.setOutputSerializer(GetMessage::class.java, object : ScriptOutputSerializer<GetMessage, List<RetrievedMessage>> {
            override fun serialize(command: GetMessage, output: List<RetrievedMessage>) {
                for (message in output) {
                    println(message)
                }
            }
        })
        executeScript.run()
    }
}