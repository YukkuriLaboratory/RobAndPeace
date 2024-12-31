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
import net.yukulab.robandpeace.DelegatedLogger

class LinearSearcherItem : Item(Settings().maxCount(1)) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        // ignore client side
        if (context.world.isClient) return super.useOnBlock(context)

        logger.info("Side:${context.side}, Hitpos:${context.hitPos}, blockPos:${context.blockPos}, CeiledHitpos:${context.hitPos.floor()}")

        val portalOriginPos = context.hitPos.floor()
        val exploreDirection = context.side.opposite

        val currentPos = portalOriginPos.toBlockPos().mutableCopy()
        for (i in 0..10) {
            logger.info("Place loop in $i")

            // Move to exploreDirection
            currentPos.move(exploreDirection)

            // Get state
            val currentState = context.world.getBlockState(currentPos)
            val calculatedIsAir = currentState.block == Blocks.AIR
            logger.info("Pos: $currentPos, BlockId: ${currentState.block.name}, IsAir:${currentState.isAir}, =AIR:$calculatedIsAir")

            if (calculatedIsAir) {
                logger.info("Checking upper block....")
                val extCurrentState = context.world.getBlockState(currentPos.up())
                val extCalculatedIsAir = extCurrentState.block == Blocks.AIR
                if (extCalculatedIsAir) {
                    logger.info("This block is equal as air block!")
                    logger.info("Found it! pos: $currentPos")
                    context.player?.sendMessage(Text.of("Found it! pos: $currentPos"))
                    return ActionResult.SUCCESS
                } else {
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
        private val logger by DelegatedLogger()
    }
}
