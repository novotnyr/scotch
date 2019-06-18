package command

import com.github.novotnyr.scotch.RabbitConfiguration
import com.github.novotnyr.scotch.command.DeclareBinding
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DeclareBindingTest {
    @Test
    fun test()  = runBlocking {
        val declareBinding = DeclareBinding(RabbitConfiguration(), "food", "cabbage", "cabbage")
        declareBinding.run()
        assertTrue(true)
    }
}