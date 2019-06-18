package domain

data class Exchange(
    val name: String,
    val vhost: String,
    val isDurable: Boolean,
    val isAutoDelete: Boolean,
    val isInternal: Boolean,
    val arguments: Map<String, Any>
)