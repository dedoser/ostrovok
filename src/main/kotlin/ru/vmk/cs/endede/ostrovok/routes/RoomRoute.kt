package ru.vmk.cs.endede.ostrovok.routes

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import java.lang.Exception
import org.koin.ktor.ext.inject
import ru.vmk.cs.endede.ostrovok.repository.RoomElasticRepository
import ru.vmk.cs.endede.ostrovok.service.RoomCachedService

@Resource("room/{id}")
private data class RoomFindById(
    val id: Long,
)

@Resource("room/search")
private data class RoomFindFull(
    val name: String? = null,
    val description: String? = null,
    val roomType: String? = null,
    val location: String? = null,
    val fuzzy: Boolean = false,
    val limit: Int = 100,
    val offset: Int = 0,
) {
    fun getParams(): Map<String, String> {
        val params = mutableMapOf<String, String>()
        name?.let {
            params["name"] = name
        }
        description?.let {
            params["description"] = description
        }
        roomType?.let {
            params["room_type"] = roomType
        }
        location?.let {
            params["location"] = location
        }

        return params
    }
}

fun Route.roomRouting() {

    val roomService by inject<RoomCachedService>()
    val roomElasticRepository by inject<RoomElasticRepository>()

    get<RoomFindById> {(id) ->
        try {
            val room = roomService.getRoom(id)
            if (room == null) {
                call.respond("Cannot find room with id=$id")
            } else {
                call.respond(room)
            }
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.ServiceUnavailable, ex.message!!)
        }
    }

    get<RoomFindFull> {req ->
        try {
            val body = try {
                call.receive<Map<String, String>>()
            } catch (ex: Exception) {
                null
            }
            val rooms = roomElasticRepository.findByFields(
                req.getParams(),
                req.fuzzy,
                req.limit,
                req.offset,
                body
            )
            call.respond(rooms)
        } catch (ex: Exception) {
            call.respond(HttpStatusCode.ServiceUnavailable, ex.message!!)
            ex.printStackTrace()
        }
    }
}