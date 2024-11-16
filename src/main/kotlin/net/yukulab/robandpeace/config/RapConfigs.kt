package net.yukulab.robandpeace.config

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer

object RapConfigs {
    @JvmStatic
    val serverConfig: RapServerConfig
        get() = AutoConfig.getConfigHolder(RapServerConfig::class.java).config

    fun init() {
        AutoConfig.register(RapServerConfig::class.java, ::Toml4jConfigSerializer)
    }
}
