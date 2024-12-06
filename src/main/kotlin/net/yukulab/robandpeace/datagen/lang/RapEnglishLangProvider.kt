package net.yukulab.robandpeace.datagen.lang

import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import net.yukulab.robandpeace.config.RapServerConfig
import net.yukulab.robandpeace.item.PickingToolItem
import net.yukulab.robandpeace.item.RapItems

class RapEnglishLangProvider(dataGenerator: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) : RapLangProvider(dataGenerator, "en_us", registryLookup) {
    override fun TranslationBuilder.generateTranslations(registryLookup: RegistryWrapper.WrapperLookup) {
        add(RapServerConfig.KEY_DISABLE_ATTACKING_IN_COOL_TIME, "Disable attacking in cool time")
        add(RapServerConfig.KEY_STEAL_COOL_TIME, "Steal cool time(Tick)")
        add(RapServerConfig.KEY_STEAL_COOL_TIME_ON_SUCCESS, "on success")
        add(RapServerConfig.KEY_STEAL_COOL_TIME_ON_FAILURE, "on failure")
        add(RapServerConfig.KEY_STEAL_CHANCES, "Steal chances")
        add(RapServerConfig.KEY_STEAL_CHANCES_FRIENDLY, "Friendly")
        add(RapServerConfig.KEY_STEAL_CHANCES_HOSTILE, "Hostile")
        add(RapServerConfig.KEY_STEAL_CHANCES_BOSS, "Boss")
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
        add(RapServerConfig.KEY_ITEMS_WOODEN_GLOVE, "Wooden Glove")
        add(RapServerConfig.KEY_ITEMS_STONE_GLOVE, "Stone Glove")
        add(RapServerConfig.KEY_ITEMS_IRON_GLOVE, "Iron Glove")
        add(RapServerConfig.KEY_ITEMS_GOLDEN_GLOVE, "Golden Glove")
        add(RapServerConfig.KEY_ITEMS_DIAMOND_GLOVE, "Diamond Glove")
        add(RapServerConfig.KEY_ITEMS_NETHERITE_GLOVE, "Netherite Glove")

        add(RapItems.ITEM_GROUP_KEY, "RobAndPeace")
        add(RapItems.SMOKE, "Smoke")
        add(RapItems.MAGIC_HAND, "Magic Hand")
        add(RapItems.PICKING_TOOL, "Picking Tool")
        add(RapItems.TRIAL_PICKING_TOOL, "Trial Picking Tool")
        add(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.SUFFIX_OMINOUS, "Ominous Picking Tool")
    }
}
