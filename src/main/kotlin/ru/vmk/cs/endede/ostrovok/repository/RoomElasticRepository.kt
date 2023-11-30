package ru.vmk.cs.endede.ostrovok.repository

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders
import ru.vmk.cs.endede.ostrovok.model.Room


class RoomElasticRepository(
    private val client: ElasticsearchClient,
    private val indexName: String,
) {

    fun findRoom(
        id: Long,
    ): Room? {
        val resp = client.get({ it.index(indexName).id(id.toString()) }, Room::class.java)
        return resp.source()
    }

    fun findByFields(
        fields: Map<String, String>,
        isFuzzy: Boolean,
        limit: Int,
        start: Int,
        features: Map<String, String>? = null,
    ): List<Room> {
        val boolQueryBuilder = QueryBuilders.bool()
        val filters: List<Query> = fields.map { (field, value) ->
            if (isFuzzy) {
                Query.Builder().fuzzy(QueryBuilders.fuzzy().field(field).value(value).build()).build()
            } else {
                Query.Builder().term(QueryBuilders.term().field(field).value(value).build()).build()
            }
        }
        val featuresFilter = features?.map { (field, value) ->
            if (isFuzzy) {
                Query.Builder().fuzzy(QueryBuilders.fuzzy().field("features.$field").value(value).build()).build()
            } else {
                Query.Builder().term(QueryBuilders.term().field("features.$field").value(value).build()).build()
            }
        } ?: emptyList()

        boolQueryBuilder.filter(filters + featuresFilter)
        val resp = client.search({
            it.index(indexName)
                .query { q ->
                    q.bool(boolQueryBuilder.build())
                }
                .size(limit)
                .from(start)
        }, Room::class.java)
        return resp.hits().hits()
            .mapNotNull { it.source() }
    }
}