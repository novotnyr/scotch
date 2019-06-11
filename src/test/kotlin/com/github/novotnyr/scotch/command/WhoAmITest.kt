package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class WhoAmITest {
    @Test
    fun successfulWhoAmi() {
        val configuration = RabbitConfiguration()
        configuration.user = ("guest")
        configuration.password = ("guest")
        configuration.virtualHost = ("/")
        configuration.port = 15672

        val command = WhoAmI(configuration)
        val whoAmIFuture = command.run()

        Awaitility.await().until(whoAmIFuture::isDone);

        val authenticatedUser = whoAmIFuture.get()
        assertEquals("guest", authenticatedUser.name)
        assertEquals("administrator", authenticatedUser.tags)
    }
}