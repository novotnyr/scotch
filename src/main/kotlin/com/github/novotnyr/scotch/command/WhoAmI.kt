package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.domain.AuthenticatedUser
import com.github.novotnyr.scotch.http.HttpClientFactory
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Details of the currently authenticated user.
 * <p>
 *     This works for RabbitMQ 3.7 and newer.
 * </p>
 */
class WhoAmI : AbstractScriptableCommand<AuthenticatedUser> {
    constructor(rabbitConfiguration: RabbitConfiguration, httpClientFactory: HttpClientFactory) : super(
        rabbitConfiguration,
        httpClientFactory
    )

    constructor(rabbitConfiguration: RabbitConfiguration) : super(rabbitConfiguration)

    override val typeToken: Type
        get() = TYPE_TOKEN

    override val urlSuffix: String
        get() = "/whoami"

    companion object {
        private val TYPE_TOKEN = object : TypeToken<AuthenticatedUser>() {}.type
    }
}
