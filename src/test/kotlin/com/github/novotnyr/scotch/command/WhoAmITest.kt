package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WhoAmITest {
    @Test
    fun successfulWhoAmi() = runBlocking {
        val configuration = RabbitConfiguration()
        configuration.user = ("guest")
        configuration.password = ("guest")
        configuration.virtualHost = ("/")
        configuration.port = 15672

        val authenticatedUser = WhoAmI(configuration).run()
        assertEquals("guest", authenticatedUser.name)
        assertEquals("administrator", authenticatedUser.tags)
    }
}