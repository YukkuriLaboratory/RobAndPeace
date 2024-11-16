package test.player

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest
import net.minecraft.entity.EntityType
import net.minecraft.test.GameTest
import net.minecraft.test.TestContext
import net.minecraft.world.GameMode

class TestPlayerAttack : FabricGameTest {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    fun checkPlayerAttackDisabled(context: TestContext) {
        val player = context.createMockPlayer(GameMode.SURVIVAL)
        val target = context.spawnMob(EntityType.PIG, 0, 0, 0)
        val currentHealth = target.health
        context.addFinalTask {
            player.attack(target)
            context.assertEquals(target.health, currentHealth, "Target health changed")
            context.assertEquals(target.primeAdversary, player, "Target prime adversary changed")
        }
    }
}
