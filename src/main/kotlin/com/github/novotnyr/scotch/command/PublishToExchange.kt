package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.ExchangeOrVirtualHostNotFoundException
import com.github.novotnyr.scotch.MissingMandatoryFieldException
import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.command.api.ErrorResponse
import com.github.novotnyr.scotch.command.api.PublishToExchangeRequest
import com.github.novotnyr.scotch.command.api.PublishToExchangeResponse
import com.github.novotnyr.scotch.http.UrlEncoder
import com.google.gson.reflect.TypeToken
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.collections.LinkedHashMap

class PublishToExchange(rabbitConfiguration: RabbitConfiguration) :
    AbstractScriptableCommand<PublishToExchangeResponse>(rabbitConfiguration) {

    var exchange = "amq.default"

    var routingKey: String? = null

    var base64Contents: String? = null

    var contentType: String? = null

    var replyTo: String? = null

    var headers: Map<String, String> = LinkedHashMap()

    override val urlSuffix: String
        get() = "/exchanges/" + UrlEncoder.encode(virtualHost) + "/" + this.exchange + "/publish"

    private val requestBody: RequestBody
        get() {
            val request = PublishToExchangeRequest()
            request.routingKey = this.routingKey
            request.base64Payload = this.base64Contents
            request.contentType = this.contentType
            request.replyTo = this.replyTo
            if (!this.headers.isEmpty()) {
                request.headers = this.headers
            }

            val jsonRequest = gson.toJson(request)

            return RequestBody.create(JSON, jsonRequest)
        }

    override val typeToken: Type
        get() = TYPE_TOKEN

    override fun buildRequest(): Request {
        return Request.Builder()
            .url(resolveUrl())
            .post(requestBody)
            .build()
    }

    override fun handleFailedResponse(response: Response, responseBodyString: String) {
        if (response.code() == 404) {
            throw ExchangeOrVirtualHostNotFoundException(this.virtualHost, this.exchange, responseBodyString)
        }
        if (response.code() == 400) {
            val errorResponse = gson.fromJson<ErrorResponse>(responseBodyString, ErrorResponse::class.java)
            if (errorResponse.reason.contains("key_missing,routing_key")) {
                throw MissingMandatoryFieldException("Routing Key")
            }
            if (errorResponse.reason.contains("key_missing,payload")) {
                throw MissingMandatoryFieldException("Payload")
            }
        }
        super.handleFailedResponse(response, responseBodyString)
    }

    fun encodeAndSetUtf8Contents(plainContents: String) {
        val base64 = Base64.getEncoder().encodeToString(plainContents.toByteArray(StandardCharsets.UTF_8))
        this.base64Contents = base64
    }

    fun encodeAndSetUtf8Contents(plainContents: ByteArray) {
        val base64 = Base64.getEncoder().encodeToString(plainContents)
        this.base64Contents = base64
    }

    companion object {
        private val TYPE_TOKEN = object : TypeToken<PublishToExchangeResponse>() {}.type
    }
}
