package ru.vmk.cs.endede.ostrovok.configuration

import com.mongodb.client.MongoClients
import org.koin.dsl.module
import org.springframework.data.mongodb.core.MongoTemplate

val mongoClient = module {
    single {
        MongoTemplate(MongoClients.create("mongodb://localhost:27017"), "test")
    }
}
