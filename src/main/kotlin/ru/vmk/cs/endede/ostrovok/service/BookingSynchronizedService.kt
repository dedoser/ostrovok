package ru.vmk.cs.endede.ostrovok.service

import com.hazelcast.core.HazelcastInstance
import java.time.LocalDate
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import ru.vmk.cs.endede.ostrovok.model.Booking
import ru.vmk.cs.endede.ostrovok.model.BookingStatus
import ru.vmk.cs.endede.ostrovok.repository.BookingRepository
import kotlin.Exception

class BookingSynchronizedService(
    private val bookingRepository: BookingRepository,
    hazelcastInstance: HazelcastInstance,
) {
    private val semaphoreBooking = hazelcastInstance.getMap<Long, Booking>(HazelcastCollectionType.BOOKING.name)

    companion object {
        private val logger = LoggerFactory.getLogger(BookingSynchronizedService::class.java)
        private val NOT_DECLINED_STATUSES = BookingStatus.entries
            .filter { it != BookingStatus.CANCELLED }
            .toSet()
    }

    fun createBooking(
        uid: Long,
        roomId: Long,
        fromDate: LocalDate,
        toDate: LocalDate,
    ): Booking {
        if (isBookingExists(roomId, fromDate, toDate)) {
            throw IllegalArgumentException("Room(id=$roomId) is already booked for date [$fromDate, $toDate]")
        }
        val order = Booking(
            uid = uid,
            roomId = roomId,
            status = BookingStatus.CREATED,
            fromDate = fromDate,
            toDate = toDate,
            createTime = LocalDateTime.now(),
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

    private fun isBookingExists(
        roomId: Long,
        fromDate: LocalDate,
        toDate: LocalDate
    ): Boolean {
        val booking = bookingRepository.getBookingsForRoomBetweenDates(
            roomId,
            fromDate,
            toDate,
            NOT_DECLINED_STATUSES
        )
        return booking.isNotEmpty()
    }

    fun getBooking(id: String): Booking? {
        return bookingRepository.getBookingById(id)
    }

    fun getBookingsForUser(uid: Long): List<Booking>{
        return bookingRepository.getBookingsForUser(uid)
    }

    fun updateBooking(
        id: String,
        status: BookingStatus,
    ) {
        try {
            val booking = bookingRepository.getBookingById(id)
                ?: throw IllegalStateException("Cannot find booking with id=$id")
            val updatedBooking = booking.copy(status = status)
            bookingRepository.updateBookingStatus(updatedBooking)
        } catch (ex: Exception) {
            logger.error("Cannot update booking(id=$id)", ex)
            throw ex
        }

    }
}