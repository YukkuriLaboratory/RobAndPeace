package net.yukulab.robandpeace.datagen.lang

import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import net.yukulab.robandpeace.config.RapServerConfig
import net.yukulab.robandpeace.entity.RapEntityType
import net.yukulab.robandpeace.item.HiddenTreasureItem
import net.yukulab.robandpeace.item.PickingToolItem
import net.yukulab.robandpeace.item.PortalHoopItem
import net.yukulab.robandpeace.item.RapItems

class RapEnglishLangProvider(dataGenerator: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) : RapLangProvider(dataGenerator, "en_us", registryLookup) {
    override fun TranslationBuilder.generateTranslations(registryLookup: RegistryWrapper.WrapperLookup) {
        add(RapServerConfig.KEY_CONFIG_TITLE, "RobAndPeace Config")
        add(RapServerConfig.KEY_DISABLE_ATTACKING_IN_COOL_TIME, "Disable attacking in cool time")
        add(RapServerConfig.KEY_STEAL_COOL_TIME, "Steal cool time(Tick)")
        add(RapServerConfig.KEY_STEAL_COOL_TIME_ON_SUCCESS, "on success")
        add(RapServerConfig.KEY_STEAL_COOL_TIME_ON_FAILURE, "on failure")
        add(RapServerConfig.KEY_STEAL_CHANCES, "Steal chances")
        val formula = "chance=(1+bonus) x weight"
        add(RapServerConfig.KEY_STEAL_CHANCES_FRIENDLY, "Friendly multiply")
        add(RapServerConfig.KEY_STEAL_CHANCES_FRIENDLY.configToolTip(), formula)
        add(RapServerConfig.KEY_STEAL_CHANCES_HOSTILE, "Hostile multiply")
        add(RapServerConfig.KEY_STEAL_CHANCES_HOSTILE.configToolTip(), formula)
        add(RapServerConfig.KEY_STEAL_CHANCES_BOSS, "Boss multiply")
        add(RapServerConfig.KEY_STEAL_CHANCES_BOSS.configToolTip(), formula)
        add(RapServerConfig.KEY_STEAL_CHANCES_ENDER_DRAGON, "Ender Dragon multiply")
        add(RapServerConfig.KEY_STEAL_CHANCES_ENDER_DRAGON.configToolTip(), formula)
        add(RapServerConfig.KEY_STEAL_CHANCES_MERCHANT_TRADE_WEIGHT, "Merchant trade weight")
        add(
            RapServerConfig.KEY_STEAL_CHANCES_MERCHANT_TRADE_WEIGHT.configToolTip(),
            "The weight of merchant/villager trade item in stealing chances",
        )
        add(RapServerConfig.KEY_STEAL_CHANCES_CRITICAL_BONUS, "Critical bonus")
        add(RapServerConfig.KEY_ANGRY_GOLEM, "Angry golem")
        add(RapServerConfig.KEY_ANGRY_GOLEM_LIVE_TIME, "Live time(Tick)")
        add(RapServerConfig.KEY_ANGRY_GOLEM_MAX_SPAWN_COUNT, "Max spawn count")
        add(RapServerConfig.KEY_ANGRY_GOLEM_SPAWN_RADIUS, "Spawn radius")
        add(RapServerConfig.KEY_ANGRY_GOLEM_SPAWN_HEIGHT, "Spawn height")
        add(RapServerConfig.KEY_ITEMS, "Items")
        add(RapServerConfig.KEY_ITEMS_SMOKE_INVISIBLE_DURATION, "Smoke default effect duration(Tick)")
        add(RapServerConfig.KEY_ITEMS_FIRE_SMOKE_EFFECT_DELAY, "Fire smoke effect delay(Tick)")
        add(RapServerConfig.KEY_ITEMS_WOODEN_GLOVE, "Wooden Glove")
        add(RapServerConfig.KEY_ITEMS_STONE_GLOVE, "Stone Glove")
        add(RapServerConfig.KEY_ITEMS_IRON_GLOVE, "Iron Glove")
        add(RapServerConfig.KEY_ITEMS_GOLDEN_GLOVE, "Golden Glove")
        add(RapServerConfig.KEY_ITEMS_DIAMOND_GLOVE, "Diamond Glove")
        add(RapServerConfig.KEY_ITEMS_NETHERITE_GLOVE, "Netherite Glove")

        // === Spider walker settings ===
        add(RapServerConfig.SpiderWalkerSettingsLang.KEY_PARENT, "Spider Walker Settings")

        // Walking
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.CONFIG_TITLE, "Spider Walker Settings - Walking")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.ALWAYS_SPRINT, "Enable always sprint")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.SIDEWAYS_SPRINT, "Enable sideways sprint")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.BACKWARDS_SPRINT, "Enable backwards sprint")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.DEFAULT_GENERIC_MOVEMENT_SPEED, "Default generic movement speed")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.SPRINT_MOVEMENT_SPEED_MULTIPLIER, "Sprint movement speed multiplier")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.STEP_HEIGHT, "Step height")

        // Jumping
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.CONFIG_TITLE, "Spider Walker Settings - Jumping")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.JUMP_STRENGTH, "Jump strength")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.COYOTE_TIME, "Coyote time")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.SMOOTH_JUMPS, "Enable smooth jumps")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.JUMP_HORIZONTAL_VELOCITY_MULTIPLIER, "Jump horizontal velocity multiplier")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.SPRINT_JUMP_HORIZONTAL_VELOCITY_MULTIPLIER, "Sprint jump horizontal velocity multiplier")

        // Wall
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.CONFIG_TITLE, "Spider Walker Settings - Wall")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_MOVEMENT, "Enable wall movement")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_DISTANCE, "Wall distance")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.STICKY_MOVEMENT, "Enable sticky movement")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_SLIDING, "Enable wall sliding")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.SLIDING_SPEED, "Sliding speed")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_CLIMBING, "Enable wall climbing")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.CLIMBING_SPEED, "Climbing speed")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.PITCH_TO_CLIMB, "Pitch to climb")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_STICKING, "Wall sticking")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_RUNNING, "Wall running")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_RUN_SLIDING_SPEED, "Wall run sliding speed")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_RUN_SPEED_BONUS, "Wall run speed bonus")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.MINIMUM_WALL_RUN_SPEED, "Minimum wall run speed")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.YAW_TO_RUN, "Yaw to run")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_JUMPING, "Wall jumping")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_JUMP_VELOCITY_MULTIPLIER, "Wall jump velocity multiplier")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_JUMP_HEIGHT, "Wall jump height")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.MINIMUM_YAW_TO_JUMP, "Minimum yaw to jump")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.JUMP_ON_LEAVING_WALL, "Jump on leaving wall")
        // =================================================================

        add(RapItems.ITEM_GROUP_KEY, "RobAndPeace")
        add(RapItems.SMOKE, "Smoke")
        add(RapItems.EXPLOSION_SMOKE, "Smoke(Explosion)")
        add(RapItems.FIRE_SMOKE, "Smoke(Fire)")
        add(RapItems.MAGIC_HAND, "Magic Hand")
        add(RapItems.ADVANCED_MAGIC_HAND, "Advanced Magic Hand")
        add(RapItems.PICKING_TOOL, "Picking Tool")
        add(RapItems.OMINOUS_PICKING_TOOL, "Ominous Picking Tool")
        add(RapItems.OMINOUS_PICKING_TOOL, PickingToolItem.SUFFIX_TRIAL, "Trial Picking Tool")
        add(RapItems.SPIDER_WALKER, "Spider Walker")
        add(RapItems.PORTAL_HOOP, "Portal Hoop(Place Mode)")
        add(RapItems.PORTAL_HOOP, PortalHoopItem.SUFFIX_REMOVE_MODE, "Portal Hoop(Remove Mode)")
        add(RapItems.HIDDEN_TREASURE, "Hidden Treasure")
        add(RapItems.HIDDEN_TREASURE, HiddenTreasureItem.SUFFIX_CONSUME, "You're King of robber!!")

        add(RapEntityType.ANGRY_GOLEM, "Angry Golem")
    }
}
