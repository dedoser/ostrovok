package ru.vmk.cs.endede.ostrovok.repository

import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.data.mongodb.core.query.and
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where
import org.springframework.data.mongodb.core.update
import ru.vmk.cs.endede.ostrovok.model.Booking
import ru.vmk.cs.endede.ostrovok.model.BookingStatus

class BookingRepository(
    private val mongoTemplate: MongoTemplate,
) {

    fun getLastBooking(
        roomId: Long,
        uid: Long? = null,
    ): Booking? {
        val criteria = where(Booking::roomId).isEqualTo(roomId)
        uid?.let {
            criteria.and(Booking::uid).isEqualTo(uid)
        }
        val query = Query.query(criteria)
            .limit(1)
            .with(Sort.by(Sort.Direction.DESC, "createTime"))
        return mongoTemplate.findOne(query)
    }

    fun getBookingById(
        id: String
    ): Booking? {
        return mongoTemplate.findOne(Query.query(where(Booking::id).isEqualTo(id)))
    }

    fun createBooking(
        order: Booking,
    ): Booking? {
        return mongoTemplate.insert(order)
    }

    fun updateBookingStatus(
        booking: Booking,
    ) {
        mongoTemplate.save(booking)
    }
}
