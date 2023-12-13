package ru.vmk.cs.endede.ostrovok.service

import java.time.LocalDate
import ru.vmk.cs.endede.ostrovok.model.Booking
import ru.vmk.cs.endede.ostrovok.model.BookingStatus
import ru.vmk.cs.endede.ostrovok.repository.BookingRepository
import ru.vmk.cs.endede.ostrovok.repository.RoomRepository
import ru.vmk.cs.endede.ostrovok.repository.UserRepository

class GeneratorService(
    private val bookingRepository: BookingRepository,
    private val roomService: RoomRepository,
    private val userRepository: UserRepository,
) {

    fun generate() {
        val today = LocalDate.now()
        for (i in 0..< 10_000) {
            val user = userRepository.getUser(i.toLong(), 1)
            val room = roomService.findRoom(i.toLong(), 1)
            var fromDate = LocalDate.of(2022, 10, 1)
            var toDate = fromDate.plusDays(1)
            while (toDate < today) {
                val order = Booking(
                    roomId = room.id,
                    uid = user.id,
                    fromDate = fromDate,
                    toDate = toDate,
                    createTime = fromDate.minusDays(1).atStartOfDay(),
                    status = getRandomStatus(),
                )
                bookingRepository.createBooking(order)
                fromDate = toDate
                toDate = toDate.plusDays(1)
            }
        }
    }

    private fun getRandomStatus(): BookingStatus {
        return BookingStatus.entries.random()
    }
}