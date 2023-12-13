package ru.vmk.cs.endede.ostrovok.routes.model

import kotlinx.serialization.Serializable
import ru.vmk.cs.endede.ostrovok.model.BookingStatus

@Serializable
data class BookingUpdateStatusRequest(
    val id: String,
    val newStatus: BookingStatus,
)
