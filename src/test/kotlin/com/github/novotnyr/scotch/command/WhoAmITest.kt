package com.github.novotnyr.scotch.command

import com.github.novotnyr.scotch.RabbitConfiguration
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class WhoAmITest {
    @Test
    fun successfulWhoAmi() = runBlocking {
        val configuration = RabbitConfiguration()
        val authenticatedUser = WhoAmI(configuration).run()
        assertEquals("guest", authenticatedUser.name)
        assertEquals("administrator", authenticatedUser.tags)
    }
}