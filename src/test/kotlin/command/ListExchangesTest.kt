package command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.command.ListExchanges
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ListExchangesTest {
    @Test
    fun test() = runBlocking {
        val configuration = RabbitConfiguration()
        val exchanges = ListExchanges(configuration).run()
        assertTrue(exchanges.isNotEmpty())
    }

    @Test
    fun testWithVHost() = runBlocking {
        val configuration = RabbitConfiguration()
        val exchanges = ListExchanges(configuration, "/").run()
        assertTrue(exchanges.all { it.vhost == "/" })
    }
}