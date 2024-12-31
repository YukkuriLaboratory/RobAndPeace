package net.yukulab.robandpeace.util

import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import net.yukulab.robandpeace.DelegatedLogger
import net.yukulab.robandpeace.RobAndPeace
import net.yukulab.robandpeace.entity.RapEntityType
import net.yukulab.robandpeace.entity.ThroughHoopPortal
import qouteall.imm_ptl.core.api.PortalAPI
import qouteall.imm_ptl.core.portal.PortalManipulation
import qouteall.q_misc_util.my_util.AARotation

data class PortalData(val origin: ThroughHoopPortal, val destination: ThroughHoopPortal)

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
        PortalManipulation.makePortalRound(portal, 18)

        val destinationPortal = PortalAPI.createReversePortal(portal)

        PortalAPI.addGlobalPortal(world, portal)
        PortalAPI.addGlobalPortal(world, destinationPortal)
        return PortalData(portal, destinationPortal)
    }

    val logger by DelegatedLogger()
}
