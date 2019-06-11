package com.github.novotnyr.scotch

class MissingMandatoryFieldException(val field: String) : RuntimeException("Missing mandatory ${field}")
