package ru.vmk.cs.endede.ostrovok.repository

import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.findOne
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.mongodb.core.query.where
import ru.vmk.cs.endede.ostrovok.model.User

class UserRepository(
    private val mongoTemplate: MongoTemplate,
) {

    companion object {
        private val logger = LoggerFactory.getLogger(UserRepository::class.java)
    }

    fun getUser(name: String): User? {
        return mongoTemplate.findOne(
            Query.query(where(User::name).isEqualTo(name)),
        )
    }

    fun getUserById(id: Long): User? {
        return mongoTemplate.findOne(
            Query.query(where(User::id).isEqualTo(id))
        )
    }

    fun saveUser(id: Long, name: String) {
        val user = User(id, name)
        saveUser(user)
    }

    fun saveUser(user: User) {
        try {
            mongoTemplate.insert(user)
        } catch (ex: Exception) {
            logger.error("Cannot save $user", ex)
            throw ex
        }
    }

    fun getUser(
        offset: Long,
        limit: Int,
    ): User {
        return mongoTemplate.findOne(Query().limit(limit).skip(offset))!!
    }
}