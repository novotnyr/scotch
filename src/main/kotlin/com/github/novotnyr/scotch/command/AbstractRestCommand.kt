package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.RabbitMqAccessDeniedException
import com.github.novotnyr.scotch.RabbitMqAdminException
import com.github.novotnyr.scotch.RabbitMqConnectionException
import com.github.novotnyr.scotch.http.DefaultHttpClientFactory
import com.github.novotnyr.scotch.http.HttpClientFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import org.slf4j.LoggerFactory
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


abstract class AbstractRestCommand<out O>(val rabbitConfiguration: RabbitConfiguration, val httpClientFactory: HttpClientFactory = DefaultHttpClientFactory()) : Command<O> {

    val logger = LoggerFactory.getLogger(javaClass)

    val JSON = MediaType.parse("application/json; charset=utf-8")

    val gson: Gson

    init {
        this.gson = buildGson()
    }

    protected fun buildGson(): Gson = Gson()

    protected val httpClient
        get() = httpClientFactory.getClient(rabbitConfiguration)

    override suspend fun run(): O {
        return suspendCancellableCoroutine { continuation ->
            val request = buildRequest()

            httpClient.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    try {
                        response.body().use { responseBody ->
                            val responseBodyString = responseBody.string()
                            if (!response.isSuccessful) {
                                if (response.code() == 401) {
                                    throw RabbitMqAccessDeniedException("Failed to execute REST API call: Access denied")
                                } else {
                                    handleFailedResponse(response, responseBodyString)
                                }
                            }
                            handleRawJson(responseBodyString)
                            val result = gson.fromJson<O>(responseBodyString, typeToken)
                            continuation.resume(result)
                        }
                    } catch (e: IOException) {
                        continuation.resumeWithException(RabbitMqAdminException("Failed to execute REST API call", e))
                    } catch (e: Throwable) {
                        continuation.resumeWithException(e)
                    }

                }

                override fun onFailure(call: Call, ioException: IOException) {
                    val exception: Exception
                    when (ioException) {
                        is ConnectException -> exception = RabbitMqConnectionException(rabbitConfiguration, ioException)
                        else -> exception = RabbitMqAdminException("Failed to execute REST API call", ioException)
                    }
                    continuation.resumeWithException(exception)
                }
            })
        }
    }

    protected open fun handleFailedResponse(response: Response, responseBodyString: String) {
        throw RabbitMqAdminException("Command failed: $responseBodyString")
    }

    protected open fun handleRawJson(json: String) {
        if (json.contains("not_authorized")) {
            throw RabbitMqAdminException("Failed to execute REST API call: Access denied")
        }
    }

    protected open fun buildRequest(): Request {
        return Request.Builder()
            .url(url)
            .get()
            .build()
    }

    protected open val url: String
        get() = baseUrl + urlSuffix

    protected abstract val urlSuffix: String

    private val baseUrl: String
        get() = "$protocol://${rabbitConfiguration.host}:${rabbitConfiguration.port}/api"

    private val protocol: String
        get() = rabbitConfiguration.protocol.name.toLowerCase()

    protected val virtualHost: String
        get() = rabbitConfiguration.virtualHost

    protected abstract val typeToken: Type

    inline fun <reified O> Gson.fromJson(json: String): O = this.fromJson<O>(json, object : TypeToken<O>() {}.type)
}