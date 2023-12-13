package ru.vmk.cs.endede.ostrovok.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.mapping.Field

@Document("Rooms")
data class Room(
    @Id
    @JsonProperty("Id")
    val id: Long,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("location")
    val location: String,
    @Field("room_type")
    @JsonProperty("room_type")
    val roomType: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("features")
    val features: Map<String, String>? = emptyMap(),
)
