package net.yukulab.robandpeace.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config

@Config(name = "robandpeace")
class RapServerConfig : ConfigData {
    @JvmField
    var disableAttackingInCoolTime: Boolean = false

    @JvmField
    var stealCoolTime: StealCoolTime = StealCoolTime()

    @JvmField
    var stealChances: StealChances = StealChances()

    @JvmField
    var angryGolemLiveTime: Int = 1200

    class StealCoolTime {
        @JvmField
        var onSuccess: Int = 1200

        @JvmField
        var onFailure: Int = 100
    }

    class StealChances {
        @JvmField
        var friendly: Int = 70

        @JvmField
        var hostile: Int = 20

        @JvmField
        var boss: Int = 1

        @JvmField
        var merchantTradeWeight: Int = 30

        @JvmField
        var criticalBonus: Int = 10
    }
}
