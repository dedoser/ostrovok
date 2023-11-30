package ru.vmk.cs.endede.ostrovok.service

import com.hazelcast.core.HazelcastInstance
import java.lang.Exception
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.slf4j.LoggerFactory
import ru.vmk.cs.endede.ostrovok.model.Booking
import ru.vmk.cs.endede.ostrovok.model.BookingStatus
import ru.vmk.cs.endede.ostrovok.repository.BookingRepository

class BookingSynchronizedService(
    private val bookingRepository: BookingRepository,
    hazelcastInstance: HazelcastInstance,
) {
    private val semaphoreBooking = hazelcastInstance.getMap<Long, Booking>(HazelcastCollectionType.BOOKING.name)

    companion object {
        private val logger = LoggerFactory.getLogger(BookingSynchronizedService::class.java)
    }

    fun createBooking(
        uid: Long,
        roomId: Long,
    ): Booking? {
        if (!isBookingExists(roomId)) {
            throw IllegalArgumentException("Room(id=$roomId) is already booked")
        }
        val order = Booking(
            uid = uid,
            roomId = roomId,
            createTime = getLocalDateTimeNow(),
            status = BookingStatus.CREATED,
        )
        if (!semaphoreBooking.tryLock(roomId)) {
            throw IllegalStateException("Room(id=$roomId) is already booked")
        }
        return try {
            bookingRepository.createBooking(order)
        } catch (ex: Exception) {
            logger.error("Cannot book room(id=${order.roomId}) for user(uid=${order.uid})", ex)
            throw ex
        } finally {
            semaphoreBooking.unlock(roomId)
        }
    }

    private fun isBookingExists(roomId: Long): Boolean {
        val booking = bookingRepository.getLastBooking(roomId)
        return booking != null && booking.status != BookingStatus.CANCELLED
    }

    fun updateBooking(
        id: String,
        status: BookingStatus,
    ) {
        val booking = bookingRepository.getBookingById(id)
            ?: throw IllegalStateException("Cannot find booking with id=$id")
        val updatedBooking = booking.copy(status = status)
        bookingRepository.updateBookingStatus(updatedBooking)
    }

    private fun getLocalDateTimeNow(): LocalDateTime {
        val now = Clock.System.now()
        return now.toLocalDateTime(TimeZone.currentSystemDefault())
    }
}