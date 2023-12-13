package ru.vmk.cs.endede.ostrovok.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "Users")
data class User(
    @Id
    val id: Long,
    val name: String,
)
