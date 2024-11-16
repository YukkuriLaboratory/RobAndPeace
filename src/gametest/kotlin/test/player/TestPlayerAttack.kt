package test.player

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest
import net.minecraft.test.GameTest
import net.minecraft.test.TestContext

class TestPlayerAttack : FabricGameTest {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    fun testPlayerAttack(context: TestContext) {
        context.addInstantFinalTask {
        }
    }
}
