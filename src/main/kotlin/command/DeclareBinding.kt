package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.http.urlEncoded
import com.google.gson.reflect.TypeToken
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.reflect.Type

class DeclareBinding(
    configuration: RabbitConfiguration,
    val exchange: String,
    val queue: String,
    val routingKey: String
) : AbstractScriptableCommand<Void>(configuration) {

    override val urlSuffix: String
        get() {
            return "/bindings/${rabbitConfiguration.virtualHost.urlEncoded}/e/${exchange.urlEncoded}/q/${queue.urlEncoded}"
        }

    override val typeToken: Type = TYPE_TOKEN

    override fun buildRequest(): Request {
        return Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
    }

    private val requestBody: RequestBody
        get() {
            val request = mapOf("routing_key" to routingKey)
            val jsonRequest = gson.toJson(request)

            return RequestBody.create(JSON, jsonRequest)
        }

    companion object {
        private val TYPE_TOKEN = object : TypeToken<Void>() {}.type
    }
}
