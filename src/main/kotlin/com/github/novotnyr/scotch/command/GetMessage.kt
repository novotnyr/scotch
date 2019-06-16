package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.QueueOrVirtualHostNotFoundException
import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.command.api.GetMessageRequest
import com.github.novotnyr.scotch.domain.RetrievedMessage
import com.github.novotnyr.scotch.http.UrlEncoder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.lang.reflect.Type

class GetMessage(val queue: String, rabbitConfiguration: RabbitConfiguration) :
    AbstractScriptableCommand<List<RetrievedMessage>>(rabbitConfiguration) {

    override val urlSuffix: String
        get() = "/queues/" + UrlEncoder.encode(virtualHost) + "/" + this.queue + "/get"

    private fun requestBody(): RequestBody {
        val payload = JsonObject()
        payload.addProperty("count", 1)
        payload.addProperty("encoding", GetMessageRequest.Encoding.AUTO.code)
        // Rabbit 3.6.x compatibility
        payload.addProperty("requeue", "false")
        // Rabbit 3.7.x compatibility
        payload.addProperty("ackmode", "ack_requeue_false")

        val jsonRequest = gson.toJson(payload)
        return RequestBody.create(JSON, jsonRequest)
    }

    override val typeToken: Type
        get() = TYPE_TOKEN

    override fun buildRequest(): Request {
        return Request.Builder()
            .url(resolveUrl())
            .post(requestBody())
            .build()
    }

    override fun handleFailedResponse(response: Response, responseBodyString: String) {
        if (response.code() == 404) {
            throw QueueOrVirtualHostNotFoundException(virtualHost, queue, responseBodyString)
        }
        super.handleFailedResponse(response, responseBodyString)
    }

    companion object {
        private val TYPE_TOKEN = object : TypeToken<Collection<RetrievedMessage>>() {}.type
    }
}
