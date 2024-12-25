package net.yukulab.robandpeace.datagen.lang

import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import net.yukulab.robandpeace.config.RapServerConfig
import net.yukulab.robandpeace.item.PickingToolItem
import net.yukulab.robandpeace.item.RapItems

class RapJapaneseLangProvider(dataGenerator: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) : RapLangProvider(dataGenerator, "ja_jp", registryLookup) {
    override fun TranslationBuilder.generateTranslations(registryLookup: RegistryWrapper.WrapperLookup) {
        add(RapServerConfig.KEY_CONFIG_TITLE, "RobAndPeace コンフィグ")
        add(RapServerConfig.KEY_DISABLE_ATTACKING_IN_COOL_TIME, "クールタイム中の攻撃を無効化")
        add(RapServerConfig.KEY_STEAL_COOL_TIME, "クールタイム(Tick)")
        add(RapServerConfig.KEY_STEAL_COOL_TIME_ON_SUCCESS, "成功時")
        add(RapServerConfig.KEY_STEAL_COOL_TIME_ON_FAILURE, "失敗時")
        add(RapServerConfig.KEY_STEAL_CHANCES, "スリ取り確率")
        val formula = "確率=(1+ボーナス)x倍率"
        add(RapServerConfig.KEY_STEAL_CHANCES_FRIENDLY, "友好MOB倍率")
        add(RapServerConfig.KEY_STEAL_CHANCES_FRIENDLY.configToolTip(), formula)
        add(RapServerConfig.KEY_STEAL_CHANCES_HOSTILE, "敵対MOB倍率")
        add(RapServerConfig.KEY_STEAL_CHANCES_HOSTILE.configToolTip(), formula)
        add(RapServerConfig.KEY_STEAL_CHANCES_BOSS, "ボス倍率")
        add(RapServerConfig.KEY_STEAL_CHANCES_BOSS.configToolTip(), formula)
        add(RapServerConfig.KEY_STEAL_CHANCES_ENDER_DRAGON, "エンダードラゴン倍率")
        add(RapServerConfig.KEY_STEAL_CHANCES_ENDER_DRAGON.configToolTip(), formula)
        add(RapServerConfig.KEY_STEAL_CHANCES_MERCHANT_TRADE_WEIGHT, "取引アイテム比率")
        add(
            RapServerConfig.KEY_STEAL_CHANCES_MERCHANT_TRADE_WEIGHT.configToolTip(),
            "行商人/村人から取引アイテムを盗める割合",
        )
        add(RapServerConfig.KEY_STEAL_CHANCES_CRITICAL_BONUS, "クリティカルボーナス")
        add(RapServerConfig.KEY_ANGRY_GOLEM, "怒ったゴーレム")
        add(RapServerConfig.KEY_ANGRY_GOLEM_LIVE_TIME, "生存時間(Tick)")
        add(RapServerConfig.KEY_ANGRY_GOLEM_MAX_SPAWN_COUNT, "最大スポーン数")
        add(RapServerConfig.KEY_ANGRY_GOLEM_SPAWN_RADIUS, "スポーン半径")
        add(RapServerConfig.KEY_ANGRY_GOLEM_SPAWN_HEIGHT, "初期位置(y)")
        add(RapServerConfig.KEY_ITEMS, "アイテム")
        add(RapServerConfig.KEY_ITEMS_SMOKE_INVISIBLE_DURATION, "煙幕のデフォルト効果時間(Tick)")
        add(RapServerConfig.KEY_ITEMS_WOODEN_GLOVE, "木のグローブ")
        add(RapServerConfig.KEY_ITEMS_STONE_GLOVE, "石のグローブ")
        add(RapServerConfig.KEY_ITEMS_IRON_GLOVE, "鉄のグローブ")
        add(RapServerConfig.KEY_ITEMS_GOLDEN_GLOVE, "金のグローブ")
        add(RapServerConfig.KEY_ITEMS_DIAMOND_GLOVE, "ダイヤモンドのグローブ")
        add(RapServerConfig.KEY_ITEMS_NETHERITE_GLOVE, "ネザライトのグローブ")

        add(RapItems.ITEM_GROUP_KEY, "RobAndPeace")
        add(RapItems.SMOKE, "煙幕")
        add(RapItems.MAGIC_HAND, "マジックハンド")
        add(RapItems.ADVANCED_MAGIC_HAND, "上級マジックハンド")
        add(RapItems.PICKING_TOOL, "ピッキングツール")
        add(RapItems.TRIAL_PICKING_TOOL, "試練のピッキングツール")
        add(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.SUFFIX_OMINOUS, "不吉なピッキングツール")
        add(RapItems.SPIDER_WALKER, "スパイダーウォーカー")
        add(RapItems.PORTAL_HOOP, "通り抜けフープ")
    }
}
