package com.github.novotnyr.scotch.http

import com.github.novotnyr.scotch.RabbitMqConnectionException
import okhttp3.OkHttpClient
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.net.ssl.SSLContext

open class DefaultSelfSignedTlsConfigurator : SelfSignedTlsConfigurator {
    override fun configureTls(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        try {
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, InsecureTrustManager.asList(), SecureRandom())
            val sslSocketFactory = sslContext.socketFactory

            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier { _, _ -> true }

            return builder
        } catch (e: NoSuchAlgorithmException) {
            throw RabbitMqConnectionException("Cannot create insecure HTTP client: " + e.message, e)
        } catch (e: KeyManagementException) {
            throw RabbitMqConnectionException("Cannot create insecure HTTP client: " + e.message, e)
        }
    }
}
