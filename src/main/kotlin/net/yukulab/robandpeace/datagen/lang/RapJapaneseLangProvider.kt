package net.yukulab.robandpeace.datagen.lang

import java.util.concurrent.CompletableFuture
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.RegistryWrapper
import net.yukulab.robandpeace.config.RapServerConfig

class RapJapaneseLangProvider(dataGenerator: FabricDataOutput, registryLookup: CompletableFuture<RegistryWrapper.WrapperLookup>) : RapLangProvider(dataGenerator, "ja_jp", registryLookup) {
    override fun TranslationBuilder.generateTranslations(registryLookup: RegistryWrapper.WrapperLookup) {
        add(RapServerConfig.KEY_DISABLE_ATTACKING_IN_COOL_TIME, "クールタイム中の攻撃を無効化")
        add(RapServerConfig.KEY_STEAL_COOL_TIME, "クールタイム(Tick)")
        add(RapServerConfig.KEY_STEAL_COOL_TIME_ON_SUCCESS, "成功時")
        add(RapServerConfig.KEY_STEAL_COOL_TIME_ON_FAILURE, "失敗時")
        add(RapServerConfig.KEY_STEAL_CHANCES, "スリ取り確率")
        add(RapServerConfig.KEY_STEAL_CHANCES_FRIENDLY, "友好MOB")
        add(RapServerConfig.KEY_STEAL_CHANCES_HOSTILE, "敵対MOB")
        add(RapServerConfig.KEY_STEAL_CHANCES_BOSS, "ボス")
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
    }
}
