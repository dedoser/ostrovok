package ru.vmk.cs.endede.ostrovok

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.resources.Resources
import io.ktor.server.routing.routing
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ru.vmk.cs.endede.ostrovok.configuration.configModule
import ru.vmk.cs.endede.ostrovok.configuration.dbServiceModule
import ru.vmk.cs.endede.ostrovok.configuration.elasticModule
import ru.vmk.cs.endede.ostrovok.configuration.hazelcastModule
import ru.vmk.cs.endede.ostrovok.configuration.mongoClient
import ru.vmk.cs.endede.ostrovok.configuration.repositoryModule
import ru.vmk.cs.endede.ostrovok.routes.bookingRoute
import ru.vmk.cs.endede.ostrovok.routes.roomRouting
import ru.vmk.cs.endede.ostrovok.routes.userRouting

fun Application.main() {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
        }
    }
    install(Koin) {
        slf4jLogger()
        modules(
            mongoClient,
            repositoryModule,
            hazelcastModule,
            dbServiceModule,
            elasticModule,
            configModule
        )
    }
    install(Resources)

    routing {
        userRouting()
        roomRouting()
        bookingRoute()
    }

}
