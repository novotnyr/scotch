package com.github.novotnyr.scotch.http

import okhttp3.OkHttpClient

interface SelfSignedTlsConfigurator {
    fun configureTls(builder: OkHttpClient.Builder): OkHttpClient.Builder = builder

    companion object {
        val EMPTY : SelfSignedTlsConfigurator = object : SelfSignedTlsConfigurator { }
    }
}

