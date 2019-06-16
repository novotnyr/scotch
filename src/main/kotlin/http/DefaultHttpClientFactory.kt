package com.github.novotnyr.scotch.http

import com.github.novotnyr.scotch.RabbitConfiguration
import okhttp3.OkHttpClient

open class DefaultHttpClientFactory(
    private val selfSignedTlsConfigurator: SelfSignedTlsConfigurator = DefaultSelfSignedTlsConfigurator()) : HttpClientFactory {

    override fun getClient(rabbitConfiguration: RabbitConfiguration): OkHttpClient {
        return getHttpClientBuilder(rabbitConfiguration).build()
    }

    open fun getHttpClientBuilder(rabbitConfiguration: RabbitConfiguration): OkHttpClient.Builder {
        var builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .authenticator(BasicAuthenticator(rabbitConfiguration.user, rabbitConfiguration.password))
            .followRedirects(false)
            .addInterceptor(LoggingOkHttpInterceptor())
        if (rabbitConfiguration.isAllowingInsecureTls) {
            builder = selfSignedTlsConfigurator.configureTls(builder)
        }
        return builder
    }
}