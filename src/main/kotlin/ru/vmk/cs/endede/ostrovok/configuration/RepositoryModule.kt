package ru.vmk.cs.endede.ostrovok.configuration

import co.elastic.clients.elasticsearch.ElasticsearchClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.vmk.cs.endede.ostrovok.repository.RoomElasticRepository
import ru.vmk.cs.endede.ostrovok.repository.RoomRepository
import ru.vmk.cs.endede.ostrovok.repository.UserRepository

val repositoryModule = module {
    singleOf(::UserRepository)
    singleOf(::RoomRepository)
    single {
        val index = get<String>(named("index_name"))
        val elasticsearchClient = get<ElasticsearchClient>()
        RoomElasticRepository(elasticsearchClient, index)
    }
}