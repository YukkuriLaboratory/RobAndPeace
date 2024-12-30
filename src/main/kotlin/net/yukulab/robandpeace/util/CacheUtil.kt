package net.yukulab.robandpeace.util

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.chunk.ChunkCache
import net.yukulab.robandpeace.DelegatedLogger

object CacheUtil {
    fun createWorldCache(world: World, startPos: BlockPos, endPos: BlockPos): ChunkCache {
        logger.info("world cached! from:$startPos -> to:$endPos")
        return ChunkCache(
            world,
            startPos,
            endPos,
        )
    }

    fun calculateAndCache(
        world: World,
        startPos: BlockPos,
        extendDirection: Direction,
        length: Int,
    ): ChunkCache = createWorldCache(world, startPos, startPos.offset(extendDirection, length))

    val logger by DelegatedLogger()
}
