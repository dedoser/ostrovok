package ru.vmk.cs.endede.ostrovok.configuration

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.HoconApplicationConfig
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.elasticsearch.client.RestClient
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.single

val elasticModule = module {
    single {
        val elasticConfig = get<HoconApplicationConfig>().config("elastic")

        val user = elasticConfig.property("user").getString()
        val password = elasticConfig.property("password").getString()
        val serverUrls = elasticConfig.property("url").getList()
        val hosts = serverUrls.map {
            HttpHost.create(it)
        }

        val credentialsProvider = BasicCredentialsProvider()
        credentialsProvider.setCredentials(AuthScope.ANY, UsernamePasswordCredentials(user, password))

        val restClient = RestClient
            .builder(*hosts.toTypedArray())
            .setHttpClientConfigCallback {
                it.disableAuthCaching()
                    .setDefaultCredentialsProvider(credentialsProvider)
            }
            .build()
        val transport = RestClientTransport(restClient, JacksonJsonpMapper())

        ElasticsearchClient(transport)
    }

    single(named("index_name")) {
        val elasticConfig = get<HoconApplicationConfig>().config("elastic")

        elasticConfig.property("index").getString()
    }
}