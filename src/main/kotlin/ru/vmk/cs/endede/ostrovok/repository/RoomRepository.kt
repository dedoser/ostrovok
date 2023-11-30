package ru.vmk.cs.endede.ostrovok.repository

import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.and
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where
import ru.vmk.cs.endede.ostrovok.model.Room

class RoomRepository(
    private val mongoTemplate: MongoTemplate
) {

    fun findRoomById(id: Long): Room? {
        return mongoTemplate.findOne(Query.query(where(Room::id).isEqualTo(id)))
    }

    fun findRoom(
        location: String,
        roomType: String,
    ): Room? {
        val criteria = where(Room::location).isEqualTo(location)
            .and(Room::roomType).isEqualTo(roomType)

        return mongoTemplate.findOne(Query.query(criteria))
    }

    fun saveRoom(room: Room) {
        mongoTemplate.insert(room)
    }
}