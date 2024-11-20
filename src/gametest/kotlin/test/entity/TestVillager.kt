package test.entity

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest
import net.minecraft.entity.EntityInteraction
import net.minecraft.test.CustomTestProvider
import net.minecraft.test.TestFunction
import net.minecraft.world.GameMode
import net.yukulab.robandpeace.DelegatedLogger
import net.yukulab.robandpeace.config.RapServerConfig
import net.yukulab.robandpeace.entity.RapEntityType
import util.TestFunction
import util.addInstantFinalTaskWithAssertion

class TestVillager : FabricGameTest {
    /**
     * 村人からスリ取りをしたときに怒ったゴーレムが期待した数生成されるかどうかを確認します。
     * このテストではゴーレムは友好度に合わせて最大5体(see [config])まで生成されます。
     * - 友好度が0以上(最近取引を25回以上している状態): 1体
     * - 友好度が0未満(初期状態): 2体
     * - 友好度が-25未満(2回殴った時): 3体
     * - 友好度が-50未満(3回殴った時): 4体
     * - 友好度が-75未満(4回以上殴った時): 5体
     */
    @CustomTestProvider
    fun shouldVillagerCallAngryGolem(): Collection<TestFunction> = (1..5).map {
        TestFunction("ShouldVillagerCallAngryGolem$it", structureName = FabricGameTest.EMPTY_STRUCTURE) { context ->
            val player = context.createMockPlayer(GameMode.SURVIVAL)
            context.addInstantFinalTaskWithAssertion(logger) {
                context.killAllEntities()
                val villager = context.spawnMob(net.minecraft.entity.EntityType.VILLAGER, 0, 0, 0)
                villager.`robandpeace$setServerConfigSupplier`(TestVillager::config)
                when (it) {
                    1 -> villager.onInteractionWith(EntityInteraction.ZOMBIE_VILLAGER_CURED, player)
                    else -> repeat(it - 2) { villager.onInteractionWith(EntityInteraction.VILLAGER_HURT, player) }
                }
                player.attack(villager)
                context.expectEntities(RapEntityType.ANGRY_GOLEM, it)
            }
        }
    }

    companion object {
        private val logger by DelegatedLogger()
        private val config = RapServerConfig().apply {
            angryGolem.maxSpawnCount = 5
            angryGolem.spawnRadius = 1
            angryGolem.spawnHeight = 1
        }
    }
}
