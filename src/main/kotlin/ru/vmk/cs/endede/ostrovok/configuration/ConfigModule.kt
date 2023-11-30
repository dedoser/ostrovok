package ru.vmk.cs.endede.ostrovok.configuration

import com.hazelcast.internal.config.ConfigLoader
import com.typesafe.config.ConfigFactory
import io.ktor.server.config.HoconApplicationConfig
import org.koin.dsl.module

val configModule = module {
    single {
        HoconApplicationConfig(ConfigFactory.load())
    }
}