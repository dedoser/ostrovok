package ru.vmk.cs.endede.ostrovok.service

import com.hazelcast.core.HazelcastInstance
import com.hazelcast.map.IMap
import java.lang.Exception
import java.util.concurrent.TimeUnit
import org.slf4j.LoggerFactory
import ru.vmk.cs.endede.ostrovok.model.User
import ru.vmk.cs.endede.ostrovok.repository.UserRepository
import kotlin.random.Random

class UserCachedService(
    private val userRepository: UserRepository,
    hazelcastInstance: HazelcastInstance,
) {
    companion object {
        private val log = LoggerFactory.getLogger(UserCachedService::class.java)
    }

    private val usersCache: IMap<Long, User> = hazelcastInstance.getMap(HazelcastCollectionType.USERS.name)

    fun getUserByName(name: String): User? {
        return userRepository.getUser(name)
    }

    fun getUserById(id: Long): User? {
        return usersCache.computeIfAbsent(id) {
            userRepository.getUserById(id)
        }
    }

    fun saveUser(name: String) {
        val user = User(generateId(name), name)
        try {
            val lockAcquired = usersCache.tryLock(user.id, 15, TimeUnit.SECONDS)
            if (lockAcquired) {
                userRepository.saveUser(user)
            } else {
                throw IllegalStateException("Cannot save user with name $name - it is already saving")
            }
        } catch (ex: Exception) {
            log.error("Error while saving user $user", ex)
            throw ex
        } finally {
            usersCache.unlock(user.id)
        }
    }

    private fun generateId(name: String): Long {
        val randLong = Random(Long.MAX_VALUE).nextLong()
        return "$randLong$name".hashCode().toLong()
    }
}