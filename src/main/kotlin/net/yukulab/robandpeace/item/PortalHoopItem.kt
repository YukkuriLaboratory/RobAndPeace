package net.yukulab.robandpeace.item

import net.minecraft.block.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemUsageContext
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
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

        // TODO Fix need on implement gravity item
        // n:z-,s:z+ / w:x-,e:x+
        val portalBasePos = context.blockPos.add(side.offsetX, side.offsetY, side.offsetZ)
        val portalExtendPos: BlockPos = getExtendPos(side, context.horizontalPlayerFacing, portalBasePos)

        context.world.setBlockState(portalBasePos, Blocks.STONE.defaultState)
        context.world.setBlockState(portalExtendPos, Blocks.HAY_BLOCK.defaultState)

        logger.info("BasePos: {}, ExtendPos: {}", portalBasePos, portalExtendPos)

        // Yの+1は、プレイヤーの高さの2ブロックで探査する都合上
        val cacheEndPos = context.blockPos.add(-side.offsetX * maxRange, -side.offsetY * maxRange + 1, -side.offsetZ * maxRange)
        val worldCache = ChunkCache(
            context.world,
            context.blockPos,
            cacheEndPos,
        )

        // Search air
        val searchPos = context.blockPos.mutableCopy()
        var flag = false
        for (i in 1..maxRange) {
            searchPos.move(-side.offsetX, -side.offsetY, -side.offsetZ)
            val state = worldCache.getBlockState(searchPos)
            logger.info("Pos: {}, IsAir: {}", searchPos, state.isAir)
            if (state.isAir) {
                logger.info("Air found!")
                context.player!!.sendMessage(Text.of("Found air!"))
                flag = true
                break
            }
        }

        if (!flag) {
            logger.info("Air not found in range. rangeSize: $maxRange")
            context.player!!.sendMessage(Text.of("Air not found in range."))

            return ActionResult.FAIL
        }

        context.world.setBlockState(searchPos, Blocks.STONE.defaultState)
        context.world.setBlockState(searchPos.add(0, 1, 0), Blocks.HAY_BLOCK.defaultState)

        // val portal: Portal = Portal.ENTITY_TYPE.create(context.world) ?: error("Failed to create portal")
        val portal: ThroughHoopPortal = RapEntityType.THROUGH_HOOP_PORTAL.create(context.world) ?: error("Failed to create portal")
        portal.originPos = portalBasePos.toBottomCenterPos().add(0.0, 1.0, 0.0)
        portal.destDim = (context.player ?: error("Failed to get player dimension registrykey")).world.registryKey
        portal.setOrientationAndSize(
            Vec3d(1.0, 0.0, 0.0),
            Vec3d(0.0, 1.0, 0.0),
            1.0,
            2.0,
        )
        portal.destination = searchPos.toBottomCenterPos().add(0.0, 1.0, 0.0)
        PortalManipulation.makePortalRound(portal, 20)
        context.world.spawnEntity(portal)
        context.world.spawnEntity(PortalAPI.createReversePortal(portal))

        return ActionResult.SUCCESS
    }
}
