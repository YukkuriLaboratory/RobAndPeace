package net.yukulab.robandpeace.extension

import net.minecraft.block.Block
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.minecraft.world.chunk.ChunkCache

fun World.setBlock(pos: BlockPos, block: Block) = setBlockState(pos, block.defaultState)

fun World.getCache(startPos: BlockPos, endPos: BlockPos, extendDirection: Direction): ChunkCache = if (startPos > endPos) {
    ChunkCache(this, endPos, startPos.offset(extendDirection))
} else {
    ChunkCache(this, startPos, endPos.offset(extendDirection))
}
