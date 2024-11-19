package util

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest
import net.minecraft.test.TestContext
import net.minecraft.test.TestFunction as MinecraftTestFunction
import net.minecraft.util.BlockRotation

/**
 * Related:
 *  - [net.fabricmc.fabric.mixin.gametest.TestFunctionsMixin]
 *  - [net.minecraft.test.GameTest]
 */
@Suppress("UnstableApiUsage", "ktlint:standard:function-naming")
fun TestFunction(
    /**
     * Recommended $className.$methodName
     */
    templatePath: String,
    batchId: String = "defaultBatch",
    structureName: String = FabricGameTest.EMPTY_STRUCTURE,
    rotation: BlockRotation = BlockRotation.NONE,
    tickLimit: Int = 100,
    duration: Long = 0,
    required: Boolean = true,
    requiredSuccesses: Int = 1,
    maxAttempts: Int = 1,
    skyAccess: Boolean = false,
    manualOnly: Boolean = false,
    starter: (context: TestContext) -> Unit,
): MinecraftTestFunction = MinecraftTestFunction(
    batchId,
    templatePath,
    structureName,
    rotation,
    tickLimit,
    duration,
    required,
    manualOnly,
    maxAttempts,
    requiredSuccesses,
    skyAccess,
    starter,
)
