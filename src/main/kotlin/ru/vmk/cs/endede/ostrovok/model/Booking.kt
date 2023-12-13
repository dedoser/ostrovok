package ru.vmk.cs.endede.ostrovok.model

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDate
import java.time.LocalDateTime
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

enum class BookingStatus {
    CREATED,
    PAYED,
    CANCELLED;

    companion object {
        @JvmStatic
        @JsonCreator
        fun fromString(value: String): BookingStatus {
            return BookingStatus.valueOf(value)
        }
    }
}

@Document(collection = "Bookings")
data class Booking(
    @Id
    val id: String? = null,
    val roomId: Long,
    val uid: Long,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val createTime: LocalDateTime,
    val status: BookingStatus
)
