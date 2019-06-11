package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.domain.AuthenticatedUser
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

/**
 * Details of the currently authenticated user.
 * <p>
 *     This works for RabbitMQ 3.7 and newer.
 * </p>
 */
class WhoAmI(rabbitConfiguration: RabbitConfiguration) : AbstractRestCommand<AuthenticatedUser>(rabbitConfiguration) {
    override val typeToken: Type
        get() = TYPE_TOKEN

    override val urlSuffix : String
        get() = "/whoami"

    companion object {
        private val TYPE_TOKEN = object : TypeToken<AuthenticatedUser>() {}.type
    }
}
