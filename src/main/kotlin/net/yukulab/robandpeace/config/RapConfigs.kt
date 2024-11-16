package net.yukulab.robandpeace.config

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer

object RapConfigs {
    fun init() {
        AutoConfig.register(RapServerConfig::class.java, ::Toml4jConfigSerializer)
    }
}
