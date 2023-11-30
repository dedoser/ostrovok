package ru.vmk.cs.endede.ostrovok.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

enum class BookingStatus {
    CREATED,
    PAYED,
    CANCELLED,
}

@Serializable
@Document(collection = "Bookings")
data class Booking(
    @Id
    val id: String? = null,
    val roomId: Long,
    val uid: Long,
    val createTime: LocalDateTime,
    val status: BookingStatus
)
