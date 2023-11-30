package ru.vmk.cs.endede.ostrovok.service

import com.hazelcast.core.HazelcastInstance
import java.lang.Exception
import org.slf4j.LoggerFactory
import ru.vmk.cs.endede.ostrovok.model.Room
import ru.vmk.cs.endede.ostrovok.repository.RoomRepository

class RoomCachedService(
    private val roomRepository: RoomRepository,
    hazelcastInstance: HazelcastInstance,
) {
    private val roomCache = hazelcastInstance.getMap<Long, Room>(HazelcastCollectionType.ROOMS.name)

    companion object {
        private val logger = LoggerFactory.getLogger(RoomCachedService::class.java)
    }

    fun getRoom(id: Long): Room? {
        return roomCache.computeIfAbsent(id) {
            try {
                roomRepository.findRoomById(id)
            } catch (ex: Exception) {
                logger.error("Error while finding room(id=$id)", ex)
                throw RuntimeException("Error while finding room(id=$id)")
            }
        }
    }
}