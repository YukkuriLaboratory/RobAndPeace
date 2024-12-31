package net.yukulab.robandpeace.util

import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.yukulab.robandpeace.DelegatedLogger
import net.yukulab.robandpeace.RobAndPeace
import net.yukulab.robandpeace.entity.RapEntityType
import net.yukulab.robandpeace.entity.ThroughHoopPortal
import qouteall.imm_ptl.core.api.PortalAPI
import qouteall.imm_ptl.core.portal.PortalManipulation
import qouteall.imm_ptl.core.portal.global_portals.GlobalPortalStorage
import qouteall.q_misc_util.my_util.AARotation

data class PortalData(val origin: ThroughHoopPortal, val destination: ThroughHoopPortal)

data class OffsetData(val baseOffset: Double, val extOffset: Double)

object PortalUtil {
    fun createPortal(
        world: ServerWorld,
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

        if (RobAndPeace.isDebugMode) logger.info(
            "Attempt to place portal... -> Origin:{}, Destination:{}",
            originPos.toString(),
            destinationPos.toString(),
        )

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

        GlobalPortalStorage.get(world).apply {
            addPortal(portal)
            addPortal(destinationPortal)
        }

        return PortalData(portal, destinationPortal)
    }

    fun getOffsetData(side: Direction): OffsetData = when (side) {
        Direction.WEST, Direction.EAST -> OffsetData(0.6, -0.4)
        Direction.NORTH, Direction.SOUTH -> OffsetData(-0.4, 0.6)
        else -> error("Invalid side: $side")
    }

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

    val logger by DelegatedLogger()
}
