package net.yukulab.robandpeace.util

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.yukulab.robandpeace.DelegatedLogger

object SearchUtil {
    fun searchLinear(
        startPos: BlockPos,
        exploreDirection: Direction,
        maxLength: Int,
        isDebugMode: Boolean = false,
        isAirFunction: (pos: BlockPos) -> Boolean,
    ): Result<BlockPos> {
        val currentPos = startPos.mutableCopy()
        // Start from first position
        for (i in 0..maxLength) {
            if (isDebugMode) logger.info("Search loop $i at $currentPos")

            if (isAirFunction(currentPos)) {
                if (isDebugMode) logger.info("Found! $currentPos")
                return Result.success(currentPos)
            }

            // Move to next
            currentPos.move(exploreDirection)
        }

        return Result.failure(RuntimeException("Air block not found in range"))
    }

    val logger by DelegatedLogger()
}

fun main() {
    SearchUtil.searchLinear(BlockPos(0, 0, 0), Direction.EAST, 10, true) { pos ->
        false
    }
    SearchUtil.searchLinear(BlockPos(0, 0, 0), Direction.WEST, 10, true) { pos ->
        false
    }
    SearchUtil.searchLinear(BlockPos(0, 0, 0), Direction.SOUTH, 10, true) { pos ->
        false
    }
    SearchUtil.searchLinear(BlockPos(0, 0, 0), Direction.NORTH, 10, true) { pos ->
        false
    }
}
