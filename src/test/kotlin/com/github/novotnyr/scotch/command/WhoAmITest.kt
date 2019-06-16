package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.RabbitConfiguration.Companion.DEFAULT_HTTP_PORT
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WhoAmITest {
    @Test
    fun successfulWhoAmi() = runBlocking {
        val configuration = RabbitConfiguration(port = DEFAULT_HTTP_PORT)
        val authenticatedUser = WhoAmI(configuration).run()
        assertEquals("guest", authenticatedUser.name)
        assertEquals("administrator", authenticatedUser.tags)
    }
}