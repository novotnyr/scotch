package com.github.novotnyr.scotch.http

import com.github.novotnyr.scotch.RabbitConfiguration
import okhttp3.OkHttpClient

interface HttpClientFactory {
    fun getClient(rabbitConfiguration: RabbitConfiguration): OkHttpClient
}