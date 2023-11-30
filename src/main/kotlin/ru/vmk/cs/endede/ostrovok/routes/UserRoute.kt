package ru.vmk.cs.endede.ostrovok.routes

import io.ktor.http.HttpStatusCode
import io.ktor.resources.Resource
import io.ktor.server.application.call
import io.ktor.server.resources.get
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import ru.vmk.cs.endede.ostrovok.service.UserCachedService

@Resource("/user")
private data class UserFindByName(
    val name: String,
)

@Resource("/user")
private data class UserFindById(
    val id: Long,
)

fun Route.userRouting() {
    val userService by inject<UserCachedService>()

    get<UserFindByName> {
        val user = userService.getUserByName(it.name)
            ?: call.respond(HttpStatusCode.NotFound, "No user with name=${it.name}")
        call.respond(user)
    }

    get<UserFindById> {
        val user = userService.getUserById(it.id)
            ?: call.respond(HttpStatusCode.NotFound, "No user with id=${it.id}")
        call.respond(user)
    }
}