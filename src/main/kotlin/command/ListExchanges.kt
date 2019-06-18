package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.http.UrlEncoder
import com.google.gson.reflect.TypeToken
import domain.Exchange
import java.lang.reflect.Type

class ListExchanges(var configuration: RabbitConfiguration, val vhost: String = "") :
    AbstractScriptableCommand<List<Exchange>>(configuration) {

    override val urlSuffix: String
        get() {
            val suffix: String = if (vhost.isNotEmpty()) {
                "/" + UrlEncoder.encode(vhost)
            } else {
                ""
            }
            return "/exchanges$suffix"
        }

    override val typeToken: Type = TYPE_TOKEN

    companion object {
        private val TYPE_TOKEN = object : TypeToken<List<Exchange>>() {}.type
    }
}