package net.yukulab.robandpeace.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.yukulab.robandpeace.DelegatedLogger
import net.yukulab.robandpeace.MOD_ID
import net.yukulab.robandpeace.RobAndPeace
import net.yukulab.robandpeace.entity.ThroughHoopPortal
import net.yukulab.robandpeace.item.component.RapComponents
import net.yukulab.robandpeace.util.CacheUtil
import net.yukulab.robandpeace.util.PortalData
import net.yukulab.robandpeace.util.PortalUtil
import net.yukulab.robandpeace.util.SearchUtil
import qouteall.imm_ptl.core.McHelper
import qouteall.imm_ptl.core.api.PortalAPI
import qouteall.imm_ptl.core.portal.Portal
import qouteall.imm_ptl.core.portal.global_portals.GlobalPortalStorage

class PortalHoopItem : Item(Settings()) {
    companion object {
        private val logger by DelegatedLogger()
        private const val MAX_RANGE = 6

        const val SUFFIX_REMOVE_MODE: String = "_remove"
        val KEY_REMOVE_MODE: Identifier = Identifier.of(MOD_ID, "remove")

        val MSG_AIR_NOT_FOUND: Identifier = Identifier.of(MOD_ID, "air_not_found")
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        // If client side
        if (context.world.isClient) return super.useOnBlock(context)

        val heldStack = context.stack

        // this stack is not in remove mode = place mode
        if (heldStack.get(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE) != true) {
            // On place or First use
            if (RobAndPeace.isDebugMode) logger.info("Portal placing...")
            heldStack.set(RapComponents.PORTAL_HOOP_LAST_USED, context.world.time)
            heldStack.set(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE, true)
            return onPlacePortal(context, heldStack)
        }

        return super.useOnBlock(context)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        if (world.isClient) return super.use(world, user, hand)

        val heldStack: ItemStack = user.getStackInHand(hand)

        // If in remove mode, remove portals.
        if (heldStack.get(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE) == true) {
            // On remove
            val lastUsedTime: Long = heldStack.get(RapComponents.PORTAL_HOOP_LAST_USED) ?: -1
            if (world.time - lastUsedTime < 2) {
                return TypedActionResult.fail(heldStack)
            }

            if (RobAndPeace.isDebugMode) logger.info("Portal removing...")
            heldStack.set(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE, false)
            return onRemovePortal(world, user, heldStack)
        }

        return super.use(world, user, hand)
    }

    private fun onPlacePortal(context: ItemUsageContext, heldStack: ItemStack): ActionResult {
        if (RobAndPeace.isDebugMode) logger.info("Player hit at ${context.hitPos}")

        val clickedSide = context.side
        val cache = CacheUtil.calculateAndCache(context.world, context.blockPos, clickedSide.opposite, 10)
        val foundPos = SearchUtil.searchLinear(context.blockPos, clickedSide.opposite, 10, RobAndPeace.isDebugMode) { pos ->
            cache.getBlockState(pos).isAir && cache.getBlockState(pos.add(0, 1, 0)).isAir
        }.getOrElse {
            logger.warn("Failed to search linear: $it")
            context.player?.sendMessage(Text.of(MSG_AIR_NOT_FOUND))
            return ActionResult.FAIL
        }
        if (RobAndPeace.isDebugMode) logger.info("found! $foundPos")

        val offsetData = PortalUtil.getOffsetData(context.side)
        val originPos = context.blockPos.toCenterPos().offset(context.side, offsetData.baseOffset).add(0.0, 0.5, 0.0)
        val destinationPos = foundPos.toCenterPos().offset(context.side.opposite, offsetData.extOffset).add(0.0, 0.5, 0.0)

        val destDimKey: RegistryKey<World> = (context.player ?: error("Failed to get player dimension registrykey")).world.registryKey

        val portalData: PortalData = PortalUtil.createPortal(
            context.world,
            originPos,
            context.side,
            destDimKey,
            destinationPos,
        )

        heldStack.set(RapComponents.PORTAL_ID_ORIGIN, portalData.origin.id)
        heldStack.set(RapComponents.PORTAL_ID_DESTINATION, portalData.destination.id)

        return ActionResult.PASS
    }

    private fun onRemovePortal(world: World, user: PlayerEntity, heldStack: ItemStack): TypedActionResult<ItemStack> {
        // Get all portals
        val portals: MutableList<Portal> = GlobalPortalStorage.getGlobalPortals(world).toMutableList()
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

        if (originPortal == null || destPortal == null) {
            logger.warn("Failed to find portals")
            return TypedActionResult.fail(heldStack)
        }

        // Remove both portals
        val serverWorld = world as ServerWorld
        PortalAPI.removeGlobalPortal(serverWorld, originPortal)
        PortalAPI.removeGlobalPortal(serverWorld, destPortal)

        return TypedActionResult.pass(heldStack)
    }

    override fun getTranslationKey(stack: ItemStack?): String = if (stack?.get(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE) == true) {
        "${translationKey}${SUFFIX_REMOVE_MODE}"
    } else {
        super.getTranslationKey(stack)
    }

    private fun checkBothIsAir(blockView: BlockView, basePos: BlockPos): Boolean {
        val baseState = blockView.getBlockState(basePos)
        val extState = blockView.getBlockState(basePos.up())
        return baseState.isAir && extState.isAir
    }
}
