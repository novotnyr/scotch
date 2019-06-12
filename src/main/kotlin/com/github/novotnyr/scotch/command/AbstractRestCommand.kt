package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.RabbitMqAccessDeniedException
import com.github.novotnyr.scotch.RabbitMqAdminException
import com.github.novotnyr.scotch.RabbitMqConnectionException
import com.github.novotnyr.scotch.http.BasicAuthenticator
import com.github.novotnyr.scotch.http.InsecureTrustManager
import com.github.novotnyr.scotch.http.LoggingOkHttpInterceptor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import org.slf4j.LoggerFactory
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


abstract class AbstractRestCommand<O>(private val rabbitConfiguration: RabbitConfiguration) : Command<O> {
    val logger = LoggerFactory.getLogger(javaClass)

    val JSON = MediaType.parse("application/json; charset=utf-8")

    val gson: Gson

    init {
        this.gson = buildGson()
    }

    protected fun buildGson() : Gson {
        return Gson()
    }

    override suspend fun run(): O {
        return suspendCancellableCoroutine { continuation ->
            var builder: OkHttpClient.Builder = OkHttpClient.Builder()
                    .authenticator(BasicAuthenticator(rabbitConfiguration.user, rabbitConfiguration.password))
                    .followRedirects(false)
                    .addInterceptor(LoggingOkHttpInterceptor())
            builder = configureTls(builder)
            val client = builder.build()

            val request = buildRequest()

            client.newCall(request).enqueue(object : Callback {
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

    protected open fun configureTls(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        if (!this.rabbitConfiguration.isAllowingInsecureTls) {
            return builder
        }

        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, InsecureTrustManager.asList(), SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier { s, sslSession -> true }

            return builder
        } catch (e: NoSuchAlgorithmException) {
            throw RabbitMqConnectionException("Cannot create insecure HTTP client: " + e.message, e)
        } catch (e: KeyManagementException) {
            throw RabbitMqConnectionException("Cannot create insecure HTTP client: " + e.message, e)
        }
    }

    protected open fun buildRequest(): Request {
        return Request.Builder()
                .url(resolveUrl())
                .get()
                .build()
    }

    protected open fun resolveUrl(): String {
        return getBaseUrl().append(urlSuffix).toString()
    }

    protected abstract val urlSuffix : String

    protected open fun getBaseUrl(): StringBuilder {
        return StringBuilder()
                .append(getProtocol())
                .append("://")
                .append(rabbitConfiguration.host)
                .append(":")
                .append(rabbitConfiguration.port)
                .append("/api")
    }

    protected fun getProtocol(): String {
        return rabbitConfiguration.protocol.name.toLowerCase()
    }

    open val virtualHost : String get() = rabbitConfiguration.virtualHost

    protected abstract val typeToken : Type

    inline fun <reified O> Gson.fromJson(json: String): O = this.fromJson<O>(json, object: TypeToken<O>() {}.type)
}