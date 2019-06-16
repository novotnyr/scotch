package com.github.novotnyr.scotch.http

import okhttp3.Interceptor
import okhttp3.Response
import org.slf4j.LoggerFactory
import java.io.IOException

class LoggingOkHttpInterceptor : Interceptor {
    val logger = LoggerFactory.getLogger(javaClass)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        logger.debug("{}", request.url())
        return chain.proceed(request)
    }
}
