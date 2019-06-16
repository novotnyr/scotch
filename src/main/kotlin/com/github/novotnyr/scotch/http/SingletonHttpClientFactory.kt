package com.github.novotnyr.scotch.http

import com.github.novotnyr.scotch.RabbitConfiguration
import okhttp3.OkHttpClient

class SingletonHttpClientFactory : DefaultHttpClientFactory() {
    private val builderCache = LinkedHashMap<RabbitConfiguration, OkHttpClient.Builder>()

    override fun getClient(rabbitConfiguration: RabbitConfiguration): OkHttpClient {
        builderCache.putIfAbsent(rabbitConfiguration, getHttpClientBuilder(rabbitConfiguration))
        return builderCache[rabbitConfiguration]!!.build()
    }
}