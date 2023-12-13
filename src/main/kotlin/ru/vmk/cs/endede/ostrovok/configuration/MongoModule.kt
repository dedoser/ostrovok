package ru.vmk.cs.endede.ostrovok.configuration

import com.mongodb.client.MongoClients
import io.ktor.server.config.HoconApplicationConfig
import org.koin.dsl.module
import org.springframework.data.mongodb.core.MongoTemplate

val mongoClient = module {
    single {
        val mongoConfig = get<HoconApplicationConfig>().config("mongo")
        val connectionString = mongoConfig.property("url").getString()
        MongoTemplate(MongoClients.create(connectionString), "test")
    }
}
