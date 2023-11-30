package ru.vmk.cs.endede.ostrovok.routes

import io.ktor.server.routing.Routing
import org.koin.ktor.ext.inject
import ru.vmk.cs.endede.ostrovok.service.BookingSynchronizedService


fun Routing.bookingRoute() {

    val bookingService by inject<BookingSynchronizedService>()

}