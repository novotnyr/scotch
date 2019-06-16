package com.github.novotnyr.scotch.command.script

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.command.AbstractScriptableCommand
import com.github.novotnyr.scotch.command.Command
import com.github.novotnyr.scotch.command.GetMessage
import com.github.novotnyr.scotch.command.PublishToExchange
import org.yaml.snakeyaml.Yaml
import java.io.FileNotFoundException
import java.io.FileReader
import java.util.*

class ExecuteScript(private val rabbitConfiguration: RabbitConfiguration?) {
    var stdErr: StdErr = SystemErr()

    var rabbitConfigurationParser = RabbitConfigurationParser()

    var scriptFile: String? = null

    var excludedCommandIndices: List<Int> = ArrayList()

    var includedCommandIndices: List<Int> = ArrayList()

    private val outputSerializers = WeakHashMap<Any, Any>()

    var defaultOutputSerializer = StdErrOutputSerializer<Any>(this.stdErr)

    @Suppress("UNCHECKED_CAST")
    suspend fun run() {
        try {
            val yaml = Yaml()
            val documents = yaml.loadAll(FileReader(this.scriptFile!!)) as Iterable<Any>
            val iterator = documents.iterator()
            val rabbitConfiguration: RabbitConfiguration
            val configuration = iterator.next() as Map<String, Any>
            if (this.rabbitConfiguration != null) {
                rabbitConfiguration = this.rabbitConfiguration
            } else {
                rabbitConfiguration = this.rabbitConfigurationParser.parseConfiguration(configuration)
            }
            val script = Script(rabbitConfiguration)

            while (iterator.hasNext()) {
                val scriptDocument = iterator.next() as Map<String, Any>
                if (scriptDocument.containsKey("publish")) {
                    val publishToExchange = parsePublishToExchange(rabbitConfiguration, scriptDocument)
                    script.append(publishToExchange)
                } else if (scriptDocument.containsKey("get")) {
                    val getMessage = parseGetMessage(rabbitConfiguration, scriptDocument)
                    script.append(getMessage)
                } else {
                    stdErr.println("Unsupported command type in " + this.scriptFile!!)
                }
            }
            validate(script)
            doRun(script)
        } catch (e: FileNotFoundException) {
            stdErr.println("Cannot find script " + this.scriptFile!!)
        }

    }

    private fun validate(script: Script): Boolean {
        if (script.configuration == null) {
            stdErr.println("No RabbitMQ connection configuration in script")
            return false
        }
        if (script.commands.isEmpty()) {
            stdErr.println("No script section in script")
            return false
        }

        return true
    }

    private suspend fun doRun(script: Script) {
        var index = 0
        for (command in script.commands) {
            if (excludedCommandIndices.contains(index)) {
                continue
            }
            if (includedCommandIndices.isEmpty() || includedCommandIndices.contains(index)) {
                val result = command.run()
                if (result != null) {
                    serializeOutput(command, result)
                }
            }
            index++
        }
    }

    private fun <C : Command<O>, O> serializeOutput(command: C, result: O) {
        val scriptOutputSerializer = this.outputSerializers[command::class.java] ?: this.defaultOutputSerializer
        if (scriptOutputSerializer is ScriptOutputSerializer<*, *>) {
            @Suppress("UNCHECKED_CAST")
            scriptOutputSerializer as ScriptOutputSerializer<C, O>
            scriptOutputSerializer.serialize(command, result)
        }
    }

    private fun parsePublishToExchange(
        rabbitConfiguration: RabbitConfiguration,
        script: Map<String, Any>
    ): PublishToExchange {
        val command = PublishToExchange(rabbitConfiguration)
        parseDescription(command, script)
        command.routingKey = script["routing-key"] as String
        command.exchange = script["publish"] as String
        if (script.containsKey("json")) {
            command.encodeAndSetUtf8Contents(script["json"] as String)
            command.contentType = "application/json"
        } else {
            command.encodeAndSetUtf8Contents(script["payload"] as String)
        }
        if (script.containsKey("reply-to")) {
            command.replyTo = script["reply-to"] as String
        }
        if (script.containsKey("headers")) {
            val headers = script["headers"]
            if (headers is Map<*, *>) {
                val stringHeaders = LinkedHashMap<String, String>()

                headers
                    .forEach { (k, v) -> stringHeaders[k.toString()] = Objects.toString(v) }
                command.headers = stringHeaders
            }
        }
        return command
    }

    private fun parseGetMessage(rabbitConfiguration: RabbitConfiguration, script: Map<String, Any>): GetMessage {
        val queue = script["get"] as String
        val command = GetMessage(queue, rabbitConfiguration)
        parseDescription(command, script)
        return command
    }

    private fun <C : AbstractScriptableCommand<O>, O> parseDescription(command: C, script: Map<String, Any>): C {
        val description = script["description"]
        if (description is String) {
            command.description = description
        }
        return command
    }

    fun <C : Command<O>, O> setOutputSerializer(
        commandClass: Class<C>,
        outputSerializer: ScriptOutputSerializer<C, O>
    ) {
        this.outputSerializers[commandClass] = outputSerializer
    }

}
