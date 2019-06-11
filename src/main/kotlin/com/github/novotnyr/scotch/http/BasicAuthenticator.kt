package com.github.novotnyr.scotch.http

import okhttp3.*
import java.io.IOException

class BasicAuthenticator(private val user: String, private val password: String) : Authenticator {

    @Throws(IOException::class)
    override fun authenticate(route: Route, response: Response): Request? {
        if (response.request().header("Authorization") != null) {
            return null // Give up, we've already attempted to authenticate.
        }

        val credential = Credentials.basic(this.user, this.password)
        return response
                .request().newBuilder()
                .header("Authorization", credential)
                .build()
    }

}
