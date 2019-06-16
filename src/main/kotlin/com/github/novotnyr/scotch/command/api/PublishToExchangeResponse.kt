package com.github.novotnyr.scotch.command.api

import com.google.gson.annotations.SerializedName

data class PublishToExchangeResponse(@SerializedName("routed") val isRouted: Boolean = false)
