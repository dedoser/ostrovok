package ru.vmk.cs.endede.ostrovok.repository

import java.time.LocalDate
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.and
import org.springframework.data.mongodb.core.query.inValues
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where
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

    fun getBookingsForRoomBetweenDates(
        roomId: Long,
        fromDate: LocalDate,
        toDate: LocalDate,
        statuses: Collection<BookingStatus>,
    ): List<Booking> {
        val criteria = Criteria()
            .orOperator(
                where(Booking::fromDate).lt(toDate).and(Booking::toDate).gt(fromDate),
                where(Booking::fromDate).lt(fromDate).and(Booking::toDate).gt(fromDate),
            )
            .and(Booking::roomId).isEqualTo(roomId)
            .and(Booking::status).inValues(statuses)
        val query = Query.query(criteria)
            .limit(100)
            .with(Sort.by(Sort.Direction.DESC, "createTime"))
        return mongoTemplate.find(query)
    }

    fun getBookingsForUser(
        uid: Long,
        order: Sort.Direction = Sort.Direction.DESC,
        limit: Int = 100,
    ): List<Booking> {
        val criteria = where(Booking::uid).isEqualTo(uid)
        val query = Query.query(criteria)
            .limit(limit)
            .with(Sort.by(order, "createTime"))
        return mongoTemplate.find(query)
    }

    fun getBookingById(
        id: String
    ): Booking? {
        return mongoTemplate.findOne(Query.query(where(Booking::id).isEqualTo(id)))
    }

    fun createBooking(
        order: Booking,
    ): Booking {
        return mongoTemplate.insert(order)
    }

    fun updateBookingStatus(
        booking: Booking,
    ) {
        mongoTemplate.save(booking)
    }
}
