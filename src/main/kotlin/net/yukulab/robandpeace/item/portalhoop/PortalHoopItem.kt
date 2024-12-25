package net.yukulab.robandpeace.item.portalhoop

import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.minecraft.world.chunk.ChunkCache
import net.yukulab.robandpeace.DelegatedLogger
import net.yukulab.robandpeace.entity.RapEntityType
import net.yukulab.robandpeace.entity.ThroughHoopPortal
import net.yukulab.robandpeace.item.component.RapComponents
import qouteall.imm_ptl.core.McHelper
import qouteall.imm_ptl.core.api.PortalAPI
import qouteall.imm_ptl.core.portal.PortalManipulation
import qouteall.imm_ptl.core.portal.global_portals.GlobalPortalStorage
import qouteall.q_misc_util.my_util.AARotation

data class PortalData(val origin: ThroughHoopPortal, val destination: ThroughHoopPortal)

class PortalHoopItem : Item(Settings()) {
    companion object {
        private val logger by DelegatedLogger()
        private const val MAX_RANGE = 6

        private fun getExtendPos(side: Direction, playerFacing: Direction, basePos: BlockPos): BlockPos = when (side) {
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

        val heldStack = context.stack

        // this stack is not in remove mode = place mode
        if (heldStack.get(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE) != true) {
            // On place or First use
            heldStack.set(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE, true)
            return onPlacePortal(context, heldStack)
        }

        return super.useOnBlock(context)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val heldStack: ItemStack = user.getStackInHand(hand)

        // If in remove mode, remove portals.
        if (heldStack.get(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE) == true) {
            // On remove
            heldStack.set(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE, false)
            return onRemovePortal(world, user, heldStack)
        }

        return super.use(world, user, hand)
    }

    private fun onPlacePortal(context: ItemUsageContext, heldStack: ItemStack): ActionResult {
        logger.debug("item used on block. Yaw: {}, Facing: {}", context.playerYaw, context.horizontalPlayerFacing)

        val side = context.side
        // Logic
        logger.debug("Portal Hoop was used! side: {}, sideOffset -> (x:{}, y:{}, z:{})", side, side.offsetX, side.offsetY, side.offsetZ)

        // === Portal origin block positions ===
        val portalBasePos = context.blockPos.add(side.offsetX, side.offsetY, side.offsetZ)
        val portalExtendPos: BlockPos = getExtendPos(side, context.horizontalPlayerFacing, portalBasePos)

        if (checkAirAreaFromWorld(context.world, portalBasePos, portalExtendPos)) {
            logger.debug("You can place the portal")
        } else {
            context.player!!.sendMessage(Text.of("Can't place block!!!! (debug message)"))
            return ActionResult.FAIL
        }

        // === Search Air area ===
        val destinationPos: BlockPos = searchAirArea(
            context.world,
            context.blockPos,
            Direction.UP,
            context.horizontalPlayerFacing,
            MAX_RANGE,
        ).getOrElse {
            logger.error("Failed to search air area")
            context.player!!.sendMessage(Text.of("Air area not found."))
            return ActionResult.FAIL
        }

        // === Let's place portal ===
        val destDimKey: RegistryKey<World> = (context.player ?: error("Failed to get player dimension registrykey")).world.registryKey

        val playerDir = context.horizontalPlayerFacing
        val offsetX = 0.4 * playerDir.offsetX
        val offsetY = 0.4 * playerDir.offsetY
        val offsetZ = 0.4 * playerDir.offsetZ
        val portalData = createPortal(
            context.world,
            portalBasePos.toCenterPos().add(offsetX, 0.5 + offsetY, offsetZ),
            context.side, // TODO: check this
            destDimKey,
            destinationPos.toCenterPos().add(-offsetX, 0.5 - offsetY, -offsetZ),
        )

        // Set both portal IDs
        heldStack.set(RapComponents.PORTAL_ID_ORIGIN, portalData.origin.id)
        heldStack.set(RapComponents.PORTAL_ID_DESTINATION, portalData.destination.id)

        return ActionResult.CONSUME
    }

    private fun onRemovePortal(world: World, user: PlayerEntity, heldStack: ItemStack): TypedActionResult<ItemStack> {
        // Get all portals
        val portals = GlobalPortalStorage.getGlobalPortals(world)
        portals.addAll( // Add nearby portals
            McHelper.getEntitiesNearby(
                user,
                ThroughHoopPortal::class.java,
                15.0,
            ),
        )

        // Get portal IDs from stack
        val originId = heldStack.get(RapComponents.PORTAL_ID_ORIGIN)
        val destId = heldStack.get(RapComponents.PORTAL_ID_DESTINATION)
        if (originId == null || destId == null) {
            logger.error("Failed to get portal ids from stack")
            return TypedActionResult.fail(heldStack)
        }

        // Search origin & destination portals
        val originPortal = portals.find { p -> p.id == originId }
        val destPortal = portals.find { p -> p.id == destId }

        // Remove both portals
        val serverWorld = world as ServerWorld
        PortalAPI.removeGlobalPortal(serverWorld, originPortal)
        PortalAPI.removeGlobalPortal(serverWorld, destPortal)

        return TypedActionResult.pass(heldStack)
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
        val cacheEndPos = interactBlockPos.add(
            // Long side
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
        for (i in 1..MAX_RANGE) {
            searchPos.move(playerFacing)
            logger.debug("Searching at x:${searchPos.x}, y:${searchPos.y}, z:${searchPos.z}")
            if (checkAirArea(chunkCache, searchPos, extensionDirection)) {
                logger.debug("Air found! Pos: {}", searchPos)
                return Result.success(searchPos)
            }
        }

        // 3. Return
        logger.debug("Air not found.")
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
        logger.debug("loop baseBlockId: {}, coord: {}", baseBlockState.block.name, basePos)
        logger.debug("loop extBlockId: {}, coord: {}", extBlockState.block.name, extendPos)

        // return both blocks are air
        return baseBlockState.isAir && extBlockState.isAir
    }

    private fun checkAirAreaFromWorld(world: World, basePos: BlockPos, extensionBlockPos: BlockPos): Boolean {
        logger.debug("Base:{}, Extend:{}", basePos, extensionBlockPos)

        // Check range is cached
        if (world.isOutOfHeightLimit(basePos) || world.isOutOfHeightLimit(extensionBlockPos)) {
            error("Pos is out of bounds for chunk cache")
        }

        // return both blocks are air
        return world.getBlockState(basePos).isAir && world.getBlockState(extensionBlockPos).isAir
    }

    private fun createPortal(
        world: World,
        originPos: Vec3d,
        wallFacing: Direction,
        destinationDim: RegistryKey<World>,
        destinationPos: Vec3d,
    ): PortalData {
        val aaRot: AARotation = AARotation.getAARotationFromYZ(Direction.UP, wallFacing)

        // Create a new portal
        val portal: ThroughHoopPortal = RapEntityType.THROUGH_HOOP_PORTAL.create(world) ?: error("Failed to create portal")

        // Link the portal origin & destination
        portal.originPos = originPos
        portal.destDim = destinationDim
        portal.destination = destinationPos

        val rightDir: Direction = aaRot.transformedX
        val upDir: Direction = aaRot.transformedY

        portal.setOrientationAndSize(
            Vec3d.of(rightDir.vector),
            Vec3d.of(upDir.vector),
            1.0,
            2.0,
        )

        portal.disableDefaultAnimation()

        // Make it rounded
        PortalManipulation.makePortalRound(portal, 20)

        val destinationPortal = PortalAPI.createReversePortal(portal)

        // Spawn portals
        world.spawnEntity(portal) // Origin
        world.spawnEntity(destinationPortal) // Destination

        return PortalData(portal, destinationPortal)
    }

    private fun placeDebugBlock(world: World, posA: BlockPos, posB: BlockPos) {
        world.setBlockState(posA, Blocks.STONE.defaultState)
        world.setBlockState(posB, Blocks.HAY_BLOCK.defaultState)
    }
}
