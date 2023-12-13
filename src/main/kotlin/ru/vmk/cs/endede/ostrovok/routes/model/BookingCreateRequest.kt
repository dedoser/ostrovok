package ru.vmk.cs.endede.ostrovok.routes.model

import java.time.LocalDate

data class BookingCreateRequest(
    val roomId: Long,
    val uid: Long,
    val fromDate: LocalDate,
    val toDate: LocalDate,
)
