package net.yukulab.robandpeace.config

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.yukulab.robandpeace.RobAndPeace

object RapConfigs {
    @JvmStatic
    val serverConfig: RapServerConfig
        get() = AutoConfig.getConfigHolder(RapServerConfig::class.java).config

    fun init() {
        val serverConfig = AutoConfig.register(RapServerConfig::class.java, ::Toml4jConfigSerializer)
        serverConfig.registerSaveListener { _, config ->
            RobAndPeace.onUpdateConfig(config)
        }
        // First load
        RobAndPeace.onUpdateConfig(serverConfig.get())
    }
}
