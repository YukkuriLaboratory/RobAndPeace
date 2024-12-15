package net.yukulab.robandpeace.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.yukulab.robandpeace.DelegatedLogger
import net.yukulab.robandpeace.entity.ThroughHoopPortal
import net.yukulab.robandpeace.item.component.RapComponents
import qouteall.imm_ptl.core.McHelper
import qouteall.imm_ptl.core.api.PortalAPI
import qouteall.imm_ptl.core.portal.global_portals.GlobalPortalStorage

class PortalHoopRemoverItem : Item(Settings().maxCount(1)) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) return TypedActionResult.pass(user.getStackInHand(hand))

        logger.info("use! user: ${user.name}")

        val stack = user.getStackInHand(hand)
        val nearestPortals = McHelper.getEntitiesNearby(
            user,
            ThroughHoopPortal::class.java,
            15.0,
        )
        val portals = GlobalPortalStorage.getGlobalPortals(world)
        portals.addAll(nearestPortals)
        val originId = stack.get(RapComponents.PORTAL_ID_ORIGIN)
        val destId = stack.get(RapComponents.PORTAL_ID_DESTINATION)
        val originPortal = portals.find { p -> p.id == originId }
        val destPortal = portals.find { p -> p.id == destId }

        val serverWorld = world as ServerWorld
        PortalAPI.removeGlobalPortal(serverWorld, originPortal)
        PortalAPI.removeGlobalPortal(serverWorld, destPortal)

        user.setStackInHand(hand, ItemStack(RapItems.PORTAL_HOOP))
        return TypedActionResult.consume(stack)
    }

    companion object {
        private val logger by DelegatedLogger()
    }
}
