package net.yukulab.robandpeace.util

import com.mojang.logging.LogUtils
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

private fun pos(x: Int, z: Int): BlockPos = BlockPos(x, 0, z)

fun main() {
    val logger = LogUtils.getLogger()

    val isAirMap = mutableMapOf<BlockPos, Boolean>()
    isAirMap[pos(1, 0)] = true
    isAirMap[pos(0, 1)] = true
    isAirMap[pos(-2, 0)] = true

    SearchUtil.searchLinear(BlockPos(0, 0, 0), Direction.WEST, 10, true) { pos ->
        isAirMap.getOrDefault(pos, false)
    }.onSuccess { foundPos -> logger.info("Found pos: $foundPos") }

    // SearchUtil.searchLinear(BlockPos(0, 0, 0), Direction.EAST, 10, true) { pos ->
    //     false
    // }
    // SearchUtil.searchLinear(BlockPos(0, 0, 0), Direction.WEST, 10, true) { pos ->
    //     false
    // }
    // SearchUtil.searchLinear(BlockPos(0, 0, 0), Direction.SOUTH, 10, true) { pos ->
    //     false
    // }
    // SearchUtil.searchLinear(BlockPos(0, 0, 0), Direction.NORTH, 10, true) { pos ->
    //     false
    // }
}
