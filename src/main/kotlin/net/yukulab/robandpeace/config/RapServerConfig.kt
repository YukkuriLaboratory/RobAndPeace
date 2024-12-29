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

            // @JvmField
            // var defaultGenericMovementSpeed: Float = 0.1f
            //
            // @JvmField
            // var sprintMovementSpeedMultiplier: Float = 0.3f

            @JvmField
            var stepHeight: Float = 0.6f
        }

        class Jumping {
            // @JvmField
            // var jumpStrength: Float = 0.42f

            // I don't know what is this...
            // @JvmField
            // var coyoteTime: Int = 0

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
            var wallMovement = true

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
            var climbingSpeed = 0.085f

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
        const val KEY_ROOT = "text.autoconfig.robandpeace.option"

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

    object SpiderWalkerSettingsLang {
        @Excluded
        const val KEY_PARENT = "${KEY_ROOT}.spiderWalkerSettings"
        object Walking {

            @Excluded
            const val CONFIG_TITLE = "${KEY_PARENT}.walking"

            @Excluded
            const val ALWAYS_SPRINT = "${KEY_PARENT}.walking.alwaysSprint"

            @Excluded
            const val SIDEWAYS_SPRINT = "${KEY_PARENT}.walking.sidewaysSprint"

            @Excluded
            const val BACKWARDS_SPRINT = "${KEY_PARENT}.walking.backwardsSprint"

            @Excluded
            const val DEFAULT_GENERIC_MOVEMENT_SPEED = "${KEY_PARENT}.walking.defaultGenericMovementSpeed"

            @Excluded
            const val SPRINT_MOVEMENT_SPEED_MULTIPLIER = "${KEY_PARENT}.walking.sprintMovementSpeedMultiplier"

            @Excluded
            const val STEP_HEIGHT = "${KEY_PARENT}.walking.stepHeight"
        }

        object Jumping {

            @Excluded
            const val CONFIG_TITLE = "${KEY_PARENT}.jumping"

            @Excluded
            const val JUMP_STRENGTH = "${KEY_PARENT}.jumping.jumpStrength"

            @Excluded
            const val COYOTE_TIME = "${KEY_PARENT}.jumping.coyoteTime"

            @Excluded
            const val SMOOTH_JUMPS = "${KEY_PARENT}.jumping.smoothJumps"

            @Excluded
            const val JUMP_HORIZONTAL_VELOCITY_MULTIPLIER = "${KEY_PARENT}.jumping.jumpHorizontalVelocityMultiplier"

            @Excluded
            const val SPRINT_JUMP_HORIZONTAL_VELOCITY_MULTIPLIER = "${KEY_PARENT}.jumping.sprintJumpHorizontalVelocityMultiplier"
        }

        object Wall {

            @Excluded
            const val CONFIG_TITLE = "${KEY_PARENT}.wall"

            @Excluded
            const val WALL_MOVEMENT = "${KEY_PARENT}.wall.wallMovement"

            @Excluded
            const val WALL_DISTANCE = "${KEY_PARENT}.wall.wallDistance"

            @Excluded
            const val STICKY_MOVEMENT = "${KEY_PARENT}.wall.stickyMovement"

            @Excluded
            const val WALL_SLIDING = "${KEY_PARENT}.wall.wallSliding"

            @Excluded
            const val SLIDING_SPEED = "${KEY_PARENT}.wall.slidingSpeed"

            @Excluded
            const val WALL_CLIMBING = "${KEY_PARENT}.wall.wallClimbing"

            @Excluded
            const val CLIMBING_SPEED = "${KEY_PARENT}.wall.climbingSpeed"

            @Excluded
            const val PITCH_TO_CLIMB = "${KEY_PARENT}.wall.pitchToClimb"

            @Excluded
            const val WALL_STICKING = "${KEY_PARENT}.wall.wallSticking"

            @Excluded
            const val WALL_RUNNING = "${KEY_PARENT}.wall.wallRunning"

            @Excluded
            const val WALL_RUN_SLIDING_SPEED = "${KEY_PARENT}.wall.wallRunSlidingSpeed"

            @Excluded
            const val WALL_RUN_SPEED_BONUS = "${KEY_PARENT}.wall.wallRunSpeedBonus"

            @Excluded
            const val MINIMUM_WALL_RUN_SPEED = "${KEY_PARENT}.wall.minimumWallRunSpeed"

            @Excluded
            const val YAW_TO_RUN = "${KEY_PARENT}.wall.yawToRun"

            @Excluded
            const val WALL_JUMPING = "${KEY_PARENT}.wall.wallJumping"

            @Excluded
            const val WALL_JUMP_VELOCITY_MULTIPLIER = "${KEY_PARENT}.wall.wallJumpVelocityMultiplier"

            @Excluded
            const val WALL_JUMP_HEIGHT = "${KEY_PARENT}.wall.wallJumpHeight"

            @Excluded
            const val MINIMUM_YAW_TO_JUMP = "${KEY_PARENT}.wall.minimumYawToJump"

            @Excluded
            const val JUMP_ON_LEAVING_WALL = "${KEY_PARENT}.wall.jumpOnLeavingWall"
        }
    }
}
