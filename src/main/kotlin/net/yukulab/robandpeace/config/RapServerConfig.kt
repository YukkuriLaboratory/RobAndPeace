package net.yukulab.robandpeace.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config

@Config(name = "robandpeace")
class RapServerConfig : ConfigData {
    @JvmField
    var stealCoolTimeTick: Int = 100
}
