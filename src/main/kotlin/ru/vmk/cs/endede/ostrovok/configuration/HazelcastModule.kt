package ru.vmk.cs.endede.ostrovok.configuration

import com.hazelcast.config.Config
import com.hazelcast.config.EvictionConfig
import com.hazelcast.config.EvictionPolicy
import com.hazelcast.config.MapConfig
import com.hazelcast.config.MaxSizePolicy
import com.hazelcast.core.Hazelcast
import org.koin.dsl.module
import org.koin.dsl.onClose
import ru.vmk.cs.endede.ostrovok.service.HazelcastCollectionType

val hazelcastModule = module {
    single {
        val config = createConfig()
        Hazelcast.newHazelcastInstance(config)
    }.onClose { it?.shutdown() }
}

private fun createConfig(): Config {
    val config = Config()
        .setProperty("hazelcast.jmx", "true")
    HazelcastCollectionType.entries.forEach {
        config.addMapConfig(createCollectionCacheConfig(it))
    }

    return config
}

private fun createCollectionCacheConfig(type: HazelcastCollectionType): MapConfig {
    val evictionConfig = EvictionConfig()
        .setEvictionPolicy(EvictionPolicy.LRU)
        .setSize(2000)
        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)

    return MapConfig(type.name)
        .setBackupCount(0)
        .setEvictionConfig(evictionConfig)
        .setTimeToLiveSeconds(60)
}