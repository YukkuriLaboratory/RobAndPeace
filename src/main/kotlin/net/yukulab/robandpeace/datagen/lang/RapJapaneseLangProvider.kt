package net.yukulab.robandpeace.datagen.lang

import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import net.yukulab.robandpeace.config.RapServerConfig
import net.yukulab.robandpeace.entity.RapEntityType
import net.yukulab.robandpeace.item.PickingToolItem
import net.yukulab.robandpeace.item.PortalHoopItem
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

        // === Spider walker settings ===
        add(RapServerConfig.SpiderWalkerSettingsLang.KEY_PARENT, "スパイダーウォーカー設定")

        // Walking
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.CONFIG_TITLE, "スパイダーウォーカー設定 - ウォーキング")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.ALWAYS_SPRINT, "常に走るモードの有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.SIDEWAYS_SPRINT, "横方向に走るモードの有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.BACKWARDS_SPRINT, "後ろに走るモードの有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.DEFAULT_GENERIC_MOVEMENT_SPEED, "デフォルトの移動速度")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.SPRINT_MOVEMENT_SPEED_MULTIPLIER, "走り移動の倍率")
        add(RapServerConfig.SpiderWalkerSettingsLang.Walking.STEP_HEIGHT, "ステップの高さ")

        // Jumping
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.CONFIG_TITLE, "スパイダーウォーカー設定 - ジャンプ")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.JUMP_STRENGTH, "ジャンプの強さ")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.COYOTE_TIME, "コヨーテタイム (:<")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.SMOOTH_JUMPS, "スムーズなジャンプの有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.JUMP_HORIZONTAL_VELOCITY_MULTIPLIER, "水平方向のジャンプ速度の倍率")
        add(RapServerConfig.SpiderWalkerSettingsLang.Jumping.SPRINT_JUMP_HORIZONTAL_VELOCITY_MULTIPLIER, "水平方向の走りジャンプ速度の倍率")

        // Wall
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.CONFIG_TITLE, "スパイダーウォーカー設定 - 壁")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_MOVEMENT, "壁移動を有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_DISTANCE, "壁との距離")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.STICKY_MOVEMENT, "スティッキーな動きの有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_SLIDING, "壁スライディングを有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.SLIDING_SPEED, "壁スライディングのスピード")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_CLIMBING, "壁登りの有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.CLIMBING_SPEED, "壁登りの速度")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.PITCH_TO_CLIMB, "壁登りの勾配")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_STICKING, "壁くっつきの有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_RUNNING, "壁走りの有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_RUN_SLIDING_SPEED, "壁走りのスライディングの速度")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_RUN_SPEED_BONUS, "壁走りの速度ボーナス")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.MINIMUM_WALL_RUN_SPEED, "壁走りの最低速度")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.YAW_TO_RUN, "走る角度")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_JUMPING, "壁ジャンプの有効化")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_JUMP_VELOCITY_MULTIPLIER, "壁ジャンプ速度の倍率")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.WALL_JUMP_HEIGHT, "壁ジャンプの高さ")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.MINIMUM_YAW_TO_JUMP, "壁ジャンプの最低角度")
        add(RapServerConfig.SpiderWalkerSettingsLang.Wall.JUMP_ON_LEAVING_WALL, "ジャンプ時に壁登りをキャンセル")
        // =================================================================

        add(RapItems.ITEM_GROUP_KEY, "RobAndPeace")
        add(RapItems.SMOKE, "煙幕")
        add(RapItems.ADVANCED_SMOKE, "煙幕(爆)")
        add(RapItems.MAGIC_HAND, "マジックハンド")
        add(RapItems.ADVANCED_MAGIC_HAND, "上級マジックハンド")
        add(RapItems.PICKING_TOOL, "ピッキングツール")
        add(RapItems.TRIAL_PICKING_TOOL, "試練のピッキングツール")
        add(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.SUFFIX_OMINOUS, "不吉なピッキングツール")
        add(RapItems.SPIDER_WALKER, "スパイダーウォーカー")
        add(RapItems.PORTAL_HOOP, "通り抜けフープ(設置モード)")
        add(RapItems.PORTAL_HOOP, PortalHoopItem.SUFFIX_REMOVE_MODE, "通り抜けフープ(撤去モード)")
        add(RapItems.HIDDEN_TREASURE, "大秘宝")

        add(RapEntityType.ANGRY_GOLEM, "用心棒のゴーレム")
    }
}
