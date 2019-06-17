package command.api

import com.github.novotnyr.scotch.command.api.PublishToExchangeRequest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PublishToExchangeRequestTest {
    @Test
    fun testPropertyPropagation() {
        val json = "application/json"
        val request = PublishToExchangeRequest()
        request.headers = mapOf("id" to "1")
        request.replyTo = "reply"
        request.contentType = json
        assertEquals("1", request.properties.headers["id"])
        assertEquals(json, request.properties.contentType)
        assertEquals("reply", request.properties.replyTo)
    }
}