package net.yukulab.robandpeace.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip

@Config(name = "robandpeace")
class RapServerConfig : ConfigData {
    @JvmField
    var disableAttackingInCoolTime: Boolean = false

    @CollapsibleObject
    @JvmField
    var stealCoolTime: StealCoolTime = StealCoolTime()

    @CollapsibleObject
    @JvmField
    var stealChances: StealChances = StealChances()

    @CollapsibleObject
    @JvmField
    var angryGolem: AngryGolem = AngryGolem()

    class StealCoolTime {
        @JvmField
        var onSuccess: Int = 1200

        @JvmField
        var onFailure: Int = 100
    }

    class StealChances {
        @BoundedDiscrete(min = 0, max = 100)
        @JvmField
        var friendly: Int = 70

        @BoundedDiscrete(min = 0, max = 100)
        @JvmField
        var hostile: Int = 20

        @BoundedDiscrete(min = 0, max = 100)
        @JvmField
        var boss: Int = 1

        @BoundedDiscrete(min = 0, max = 100)
        @Tooltip
        @JvmField
        var merchantTradeWeight: Int = 30

        @JvmField
        var criticalBonus: Int = 10
    }

    class AngryGolem {
        @JvmField
        var liveTime: Int = 1200

        @JvmField
        var maxSpawnCount = 5

        @JvmField
        var spawnRadius = 8

        @JvmField
        var spawnHeight = 10
    }

    companion object {
        @Excluded
        const val KEY_DISABLE_ATTACKING_IN_COOL_TIME = "text.autoconfig.robandpeace.option.disableAttackingInCoolTime"

        @Excluded
        const val KEY_STEAL_COOL_TIME = "text.autoconfig.robandpeace.option.stealCoolTime"

        @Excluded
        const val KEY_STEAL_COOL_TIME_ON_SUCCESS = "text.autoconfig.robandpeace.option.stealCoolTime.onSuccess"

        @Excluded
        const val KEY_STEAL_COOL_TIME_ON_FAILURE = "text.autoconfig.robandpeace.option.stealCoolTime.onFailure"

        @Excluded
        const val KEY_STEAL_CHANCES = "text.autoconfig.robandpeace.option.stealChances"

        @Excluded
        const val KEY_STEAL_CHANCES_FRIENDLY = "text.autoconfig.robandpeace.option.stealChances.friendly"

        @Excluded
        const val KEY_STEAL_CHANCES_HOSTILE = "text.autoconfig.robandpeace.option.stealChances.hostile"

        @Excluded
        const val KEY_STEAL_CHANCES_BOSS = "text.autoconfig.robandpeace.option.stealChances.boss"

        @Excluded
        const val KEY_STEAL_CHANCES_MERCHANT_TRADE_WEIGHT =
            "text.autoconfig.robandpeace.option.stealChances.merchantTradeWeight"

        @Excluded
        const val KEY_STEAL_CHANCES_CRITICAL_BONUS = "text.autoconfig.robandpeace.option.stealChances.criticalBonus"

        @Excluded
        const val KEY_ANGRY_GOLEM = "text.autoconfig.robandpeace.option.angryGolem"

        @Excluded
        const val KEY_ANGRY_GOLEM_LIVE_TIME = "text.autoconfig.robandpeace.option.angryGolem.liveTime"

        @Excluded
        const val KEY_ANGRY_GOLEM_MAX_SPAWN_COUNT = "text.autoconfig.robandpeace.option.angryGolem.maxSpawnCount"

        @Excluded
        const val KEY_ANGRY_GOLEM_SPAWN_RADIUS = "text.autoconfig.robandpeace.option.angryGolem.spawnRadius"

        @Excluded
        const val KEY_ANGRY_GOLEM_SPAWN_HEIGHT = "text.autoconfig.robandpeace.option.angryGolem.spawnHeight"
    }
}
