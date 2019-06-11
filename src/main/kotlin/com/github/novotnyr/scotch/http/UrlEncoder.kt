package com.github.novotnyr.scotch.http

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object UrlEncoder {
    fun encode(input: String): String {
        try {
            return URLEncoder.encode(input, StandardCharsets.UTF_8.name())
        } catch (e: UnsupportedEncodingException) {
            throw IllegalStateException("System charset (UTF-8) not found")
        }
    }
}
