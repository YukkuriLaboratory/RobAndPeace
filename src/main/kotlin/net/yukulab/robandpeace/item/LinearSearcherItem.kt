package net.yukulab.robandpeace.item

import kotlin.math.ceil
import kotlin.math.floor
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.chunk.ChunkCache
import net.yukulab.robandpeace.DelegatedLogger

class LinearSearcherItem : Item(Settings().maxCount(1)) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        // ignore client side
        if (context.world.isClient) return super.useOnBlock(context)

        logger.info("Side:${context.side}, Hitpos:${context.hitPos}, blockPos:${context.blockPos}, CeiledHitpos:${context.hitPos.floor()}")

        val portalOriginPos = context.hitPos.floor()
        val exploreDirection = context.side.opposite

        // Cache world
        val worldCache = ChunkCache(
            context.world,
            portalOriginPos.toBlockPos(),
            portalOriginPos.toBlockPos().offset(exploreDirection, SEARCH_MAX).up(),
        )

        val currentPos = portalOriginPos.toBlockPos().mutableCopy()
        for (i in 0..SEARCH_MAX) {
            logger.info("Place loop in $i")

            // Move to exploreDirection
            currentPos.move(exploreDirection)

            // Get state
            val currentState = worldCache.getBlockState(currentPos)
            val calculatedIsAir = currentState.block == Blocks.AIR
            logger.info("Pos: $currentPos, BlockId: ${currentState.block.name}, IsAir:${currentState.isAir}, =AIR:$calculatedIsAir")

            // If under block is air
            if (calculatedIsAir) {
                logger.info("Checking upper block....")
                val extCurrentState = worldCache.getBlockState(currentPos.up())
                val extCalculatedIsAir = extCurrentState.block == Blocks.AIR

                if (extCalculatedIsAir) {
                    // If upper block is air too
                    logger.info("This block is equal as air block!")
                    logger.info("Found it! pos: $currentPos")
                    context.player?.sendMessage(Text.of("Found it! pos: $currentPos"))
                    val actualDestVec = currentPos.toCenterPos().offset(exploreDirection, -0.4)
                    logger.info("Actual destination -> $actualDestVec")
                    return ActionResult.SUCCESS
                } else {
                    // If upper block wasn't air
                    logger.info("Upper block is not air. continue...")
                    continue
                }
            }
        }

        logger.info("Couldn't find air space...")

        return ActionResult.PASS
    }

    private fun Double.toCeilInt(): Int = ceil(this).toInt()

    private fun Vec3d.floor() = Vec3d(floor(x), floor(y), floor(z))

    private fun Vec3d.toBlockPos() = BlockPos(x.toCeilInt(), y.toCeilInt(), z.toCeilInt())

    companion object {
        private val SEARCH_MAX = 10
        private val logger by DelegatedLogger()
    }
}
