package net.yukulab.robandpeace.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.CollapsibleObject
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Excluded
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.Tooltip
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment

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

    @CollapsibleObject
    @JvmField
    var items: Items = Items()

    // TODO: add lang for spider walker settings
    @CollapsibleObject
    @JvmField
    var spiderWalkerSettings: SpiderWalkerSettings = SpiderWalkerSettings()

    class StealCoolTime {
        @JvmField
        var onSuccess: Int = 1200

        @JvmField
        var onFailure: Int = 100
    }

    class StealChances {
        @Tooltip
        @JvmField
        var friendly: Double = 1.0

        @Tooltip
        @JvmField
        var hostile: Double = 0.5

        @Tooltip
        @JvmField
        var boss: Double = 0.06

        @Tooltip
        @JvmField
        var enderDragon: Double = 0.02

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

    class Items {
        @JvmField
        var smokeInvisibleDuration: Int = 60

        @BoundedDiscrete(min = 0, max = 100)
        @JvmField
        var woodenGlove: Int = 5

        @BoundedDiscrete(min = 0, max = 100)
        @JvmField
        var stoneGlove: Int = 10

        @BoundedDiscrete(min = 0, max = 100)
        @JvmField
        var ironGlove: Int = 15

        @BoundedDiscrete(min = 0, max = 100)
        @JvmField
        var goldenGlove: Int = 25

        @BoundedDiscrete(min = 0, max = 100)
        @JvmField
        var diamondGlove: Int = 20

        @BoundedDiscrete(min = 0, max = 100)
        @JvmField
        var netheriteGlove: Int = 30
    }

    class SpiderWalkerSettings {
        @CollapsibleObject
        @JvmField
        var walking: Walking = Walking()

        @CollapsibleObject
        @JvmField
        var jumping: Jumping = Jumping()

        @CollapsibleObject
        @JvmField
        var wall: Wall = Wall()

        class Walking {
            @JvmField
            var alwaysSprint: Boolean = false

            @JvmField
            var sidewaysSprint: Boolean = false

            @JvmField
            var backwardsSprint: Boolean = false

            @JvmField
            var defaultGenericMovementSpeed: Float = 0.1f

            @JvmField
            var sprintMovementSpeedMultiplier: Float = 0.3f

            @JvmField
            var stepHeight: Float = 0.6f
        }

        class Jumping {
            @JvmField
            var jumpStrength: Float = 0.42f

            @JvmField
            var coyoteTime: Int = 0

            @JvmField
            var smoothJumps: Boolean = false

            @JvmField
            var jumpHorizontalVelocityMultiplier: Float = 0.0f

            @JvmField
            var sprintJumpHorizontalVelocityMultiplier = 0.2f
        }

        class Wall {
            // === general

            @JvmField
            var wallMovement = false

            @JvmField
            var wallDistance = 0.05f

            @JvmField
            var stickyMovement = true

            // === sliding

            @JvmField
            var wallSliding = true

            @JvmField
            var slidingSpeed = 0.05f

            // === climbing

            @JvmField
            var wallClimbing = true

            @JvmField
            var climbingSpeed = 0.05f

            @JvmField
            @Comment("-90.0 ~ 90.0")
            var pitchToClimb = 0.0f

            // === sticking

            @JvmField
            var wallSticking = true

            // === running

            @JvmField
            var wallRunning = true

            @JvmField
            var wallRunSlidingSpeed = 0.0f

            @JvmField
            var wallRunSpeedBonus = 0.0f

            @JvmField
            var minimumWallRunSpeed = 0.15f

            @JvmField
            @Comment("0.0 ~ 180.0")
            var yawToRun = 0.0f

            // === jumping

            @JvmField
            var wallJumping = true

            @JvmField
            var wallJumpVelocityMultiplier = 0.2f

            @JvmField
            var wallJumpHeight = 0.42f

            @JvmField
            @Comment("0.0 ~ 180.0")
            var minimumYawToJump = 91.0f

            @JvmField
            var jumpOnLeavingWall = false
        }
    }

    companion object {
        @Excluded
        const val KEY_CONFIG_TITLE = "text.autoconfig.robandpeace.title"

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
        const val KEY_STEAL_CHANCES_ENDER_DRAGON = "text.autoconfig.robandpeace.option.stealChances.enderDragon"

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

        @Excluded
        const val KEY_ITEMS = "text.autoconfig.robandpeace.option.items"

        @Excluded
        const val KEY_ITEMS_SMOKE_INVISIBLE_DURATION = "text.autoconfig.robandpeace.option.items.smokeInvisibleDuration"

        @Excluded
        const val KEY_ITEMS_WOODEN_GLOVE = "text.autoconfig.robandpeace.option.items.woodenGlove"

        @Excluded
        const val KEY_ITEMS_STONE_GLOVE = "text.autoconfig.robandpeace.option.items.stoneGlove"

        @Excluded
        const val KEY_ITEMS_IRON_GLOVE = "text.autoconfig.robandpeace.option.items.ironGlove"

        @Excluded
        const val KEY_ITEMS_GOLDEN_GLOVE = "text.autoconfig.robandpeace.option.items.goldenGlove"

        @Excluded
        const val KEY_ITEMS_DIAMOND_GLOVE = "text.autoconfig.robandpeace.option.items.diamondGlove"

        @Excluded
        const val KEY_ITEMS_NETHERITE_GLOVE = "text.autoconfig.robandpeace.option.items.netheriteGlove"
    }
}
