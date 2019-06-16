Scotch, a RabbitMQ HTTP client based on Kotlin coroutines
=========================================================

Scotch is a HTTP-based API client that uses RabbitMQ [Management Plugin](https://www.rabbitmq.com/management.html)
for basic administrative operations.

Scotch is based on nonblocking OKHTTP client and Kotlin 1.2.1+ coroutines.

Commands
========
There are multiple commands available. Each command
takes a `RabbitConfiguration` indication connection settings and credentials.

A command provides a suspending `run` method that executes the command
and returns a type-safe result.

See `WhoAmI` Command for example usage.

`WhoAmi`
--------
Returns information about authenticated user.

```kotlin
import com.github.novotnyr.scotch.command.WhoAmI
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking {
        val whoAmI = WhoAmI(RabbitConfiguration(port = RabbitConfiguration.DEFAULT_HTTP_PORT))
        val user = whoAmI.run()
        println(user.name)
    }
}
```

`GetMessage`
------------
Consumes a message from the designated queue.

`PublishToExchange`
-------------------
Publishes a message into a designated RabbitMQ exchange.

```kotlin
val publishToExchange = PublishToExchange(rabbitConfiguration = RabbitConfiguration(port = RabbitConfiguration.DEFAULT_HTTP_PORT))
publishToExchange.routingKey = "cabbage"
publishToExchange.encodeAndSetUtf8Contents("Hello")

val confirmation = publishToExchange.run()
println(if(confirmation.isRouted) "yes" else "no")
```