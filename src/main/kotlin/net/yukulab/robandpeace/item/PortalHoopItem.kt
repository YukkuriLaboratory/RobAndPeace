package net.yukulab.robandpeace.item

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.chunk.ChunkCache
import net.yukulab.robandpeace.DelegatedLogger
import net.yukulab.robandpeace.entity.RapEntityType
import net.yukulab.robandpeace.entity.ThroughHoopPortal
import qouteall.imm_ptl.core.api.PortalAPI
import qouteall.imm_ptl.core.portal.PortalManipulation

class PortalHoopItem : Item(Settings()) {
    companion object {
        val logger by DelegatedLogger()
        const val maxRange = 6

        fun getExtendPos(side: Direction, playerFacing: Direction, basePos: BlockPos): BlockPos = when (side) {
            Direction.UP -> basePos.mutableCopy().move(
                playerFacing.offsetX,
                playerFacing.offsetY,
                playerFacing.offsetZ,
            )
            Direction.DOWN -> basePos.mutableCopy().move(
                -playerFacing.offsetX,
                -playerFacing.offsetY,
                -playerFacing.offsetZ,
            )
            Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH -> basePos.add(0, 1, 0)
        }
    }
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        // If client side
        if (context.world.isClient) return super.useOnBlock(context)

        context.player!!.sendMessage(Text.of("Yaw: ${context.playerYaw}, Facing: ${context.horizontalPlayerFacing}"))

        val side = context.side
        // Logic
        logger.info("Portal Hoop was used! side: {}, sideOffset -> (x:{}, y:{}, z:{})", side, side.offsetX, side.offsetY, side.offsetZ)

        // === Portal origin block positions ===
        val portalBasePos = context.blockPos.add(side.offsetX, side.offsetY, side.offsetZ)
        val portalExtendPos: BlockPos = getExtendPos(side, context.horizontalPlayerFacing, portalBasePos)

        if (checkAirAreaFromWorld(context.world, portalBasePos, portalExtendPos)) {
            placeDebugBlock(context.world, portalBasePos, portalExtendPos)
        } else {
            context.player!!.sendMessage(Text.of("Can't place block!!!! (debug message)"))
            return ActionResult.FAIL
        }

        // logger.info("BasePos: {}, ExtendPos: {}", portalBasePos, portalExtendPos)

        // === Search Air area ===
        val destinationPos: BlockPos = searchAirArea(
            context.world,
            context.blockPos,
            Direction.UP, // because only support y+1 block as extension block. TODO: support gravity-api
            context.horizontalPlayerFacing,
            maxRange,
        ).getOrElse {
            logger.error("Failed to search air area")
            context.player!!.sendMessage(Text.of("Air area not found."))
            return ActionResult.FAIL
        }

        placeDebugBlock(context.world, destinationPos, destinationPos.add(0, 1, 0))

        // === Let's place portal ===
        val destDimKey: RegistryKey<World> = (context.player ?: error("Failed to get player dimension registrykey")).world.registryKey

        createPortal(context.world, portalBasePos.add(0, 1, 0), destDimKey, destinationPos.add(0, 1, 0))

        return ActionResult.SUCCESS
    }

    /**
     * Search air area to place the portal
     *
     * @param targetWorld world of search target
     * @param interactBlockPos blockPos of player interacted block
     * @param extensionDirection direction of which to extend the portal
     * @param playerFacing direction of player facing
     * @param maximumSearchRange maximum range of search
     * @return result of search as Result<BlockPos>
     */
    // TODO: support gravity api
    private fun searchAirArea(
        targetWorld: World,
        interactBlockPos: BlockPos,
        extensionDirection: Direction,
        playerFacing: Direction,
        maximumSearchRange: Int,
    ): Result<BlockPos> {
        // 1. Cache
        val cacheStartPos = interactBlockPos
        val cacheEndPos = interactBlockPos.add( // Long side
            playerFacing.offsetX * maximumSearchRange,
            playerFacing.offsetY * maximumSearchRange,
            playerFacing.offsetZ * maximumSearchRange,
        ).offset(extensionDirection)
        val chunkCache = ChunkCache(
            targetWorld,
            cacheStartPos,
            cacheEndPos,
        )

        // 2. Search
        val searchPos = interactBlockPos.mutableCopy()
        for (i in 1..maxRange) {
            searchPos.move(playerFacing)
            if (checkAirArea(chunkCache, searchPos, extensionDirection)) {
                logger.info("Air found! Pos: {}", searchPos)
                return Result.success(searchPos)
            }
        }

        // 3. Return
        logger.info("Air not found.")
        return Result.failure(RuntimeException("Air was not found in this area."))
    }

    private fun checkAirArea(chunkCache: ChunkCache, basePos: BlockPos, extensionDirection: Direction): Boolean {
        val extendPos = basePos.offset(extensionDirection)

        // Check range is cached
        if (chunkCache.isOutOfHeightLimit(basePos) || chunkCache.isOutOfHeightLimit(extendPos)) {
            error("Pos is out of bounds for chunk cache")
        }

        val baseBlockState: BlockState = chunkCache.getBlockState(basePos)
        val extBlockState: BlockState = chunkCache.getBlockState(extendPos)
        logger.info("loop baseBlockId: ${baseBlockState.block.name}, coord: $basePos")
        logger.info("loop extBlockId: ${extBlockState.block.name}, coord: $extendPos")

        // return both blocks are air
        return baseBlockState.isAir && extBlockState.isAir
    }

    private fun checkAirAreaFromWorld(world: World, basePos: BlockPos, extensionBlockPos: BlockPos): Boolean {
        logger.info("Base:{}, Extend:{}", basePos, extensionBlockPos)

        // Check range is cached
        if (world.isOutOfHeightLimit(basePos) || world.isOutOfHeightLimit(extensionBlockPos)) {
            error("Pos is out of bounds for chunk cache")
        }

        // return both blocks are air
        return world.getBlockState(basePos).isAir && world.getBlockState(extensionBlockPos).isAir
    }

    private fun createPortal(
        world: World,
        originPos: BlockPos,
        destinationDim: RegistryKey<World>,
        destinationPos: BlockPos,
    ): ThroughHoopPortal {
        // Create a new portal
        val portal: ThroughHoopPortal = RapEntityType.THROUGH_HOOP_PORTAL.create(world) ?: error("Failed to create portal")

        // Link the portal origin & destination
        portal.originPos = originPos.toBottomCenterPos() // TODO: .add(0.0, 1.0, 0.0)
        portal.destDim = destinationDim // TODO: (context.player ?: error("Failed to get player dimension registrykey")).world.registryKey
        portal.destination = destinationPos.toBottomCenterPos() // TODO: .add(0.0, 1.0, 0.0)

        portal.setOrientationAndSize(
            Vec3d(1.0, 0.0, 0.0),
            Vec3d(0.0, 1.0, 0.0),
            1.0,
            2.0,
        )

        // Make it rounded
        PortalManipulation.makePortalRound(portal, 20)

        // Spawn portals
        world.spawnEntity(portal) // Origin
        world.spawnEntity(PortalAPI.createReversePortal(portal)) // Destination

        return portal
    }

    private fun placeDebugBlock(world: World, posA: BlockPos, posB: BlockPos) {
        world.setBlockState(posA, Blocks.STONE.defaultState)
        world.setBlockState(posB, Blocks.HAY_BLOCK.defaultState)
    }
}
