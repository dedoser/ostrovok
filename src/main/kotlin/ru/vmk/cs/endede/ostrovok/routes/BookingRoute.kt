package ru.vmk.cs.endede.ostrovok.routes

import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject
import ru.vmk.cs.endede.ostrovok.routes.model.BookingCreateRequest
import ru.vmk.cs.endede.ostrovok.routes.model.BookingUpdateStatusRequest
import ru.vmk.cs.endede.ostrovok.service.BookingSynchronizedService

@Resource("find/{id}")
private data class BookingFindById(
    val id: String,
)
//
//@Resource("update")
//private data class BookingUpdateStatus(
//    val id: String,
//    val newStatus: BookingStatus,
//)

@Resource("{uid}/all")
private data class BookingGetAllForUser(
    val uid: Long,
)

fun Routing.bookingRoute() {

    val bookingService by inject<BookingSynchronizedService>()

    route("booking") {
        post("create") {
            val (roomId, uid, fromDate, toDate) = call.receive<BookingCreateRequest>()
            try {
                bookingService.createBooking(uid, roomId, fromDate, toDate)
            } catch (ex: Exception) {
                call.respond("Cannot create booking for room(id=$roomId). Reason - ${ex.message!!}")
            }
        }

        get<BookingFindById> { (id) ->
            val booking = bookingService.getBooking(id)
            booking?.let {
                call.respond(it)
            } ?: call.respond("Cannot find booking with id = $id")
        }

        get<BookingGetAllForUser> {(uid) ->
            val bookings = bookingService.getBookingsForUser(uid)
            call.respond(bookings)
        }

        post("update") {
            val (id, newStatus) = call.receive<BookingUpdateStatusRequest>()
            try {
                bookingService.updateBooking(id, newStatus)
                call.respond("Successfully updated")
            } catch (ex: Exception) {
                call.respond("Failure update. Reason - ${ex.message!!}")
            }
        }
    }

}