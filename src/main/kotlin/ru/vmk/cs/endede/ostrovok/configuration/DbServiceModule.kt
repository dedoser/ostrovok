package ru.vmk.cs.endede.ostrovok.configuration

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.vmk.cs.endede.ostrovok.service.BookingSynchronizedService
import ru.vmk.cs.endede.ostrovok.service.GeneratorService
import ru.vmk.cs.endede.ostrovok.service.RoomCachedService
import ru.vmk.cs.endede.ostrovok.service.UserCachedService

val dbServiceModule = module {
    singleOf(::UserCachedService)
    singleOf(::BookingSynchronizedService)
    singleOf(::RoomCachedService)
    singleOf(::GeneratorService)
}