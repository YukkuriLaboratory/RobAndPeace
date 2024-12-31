package net.yukulab.robandpeace.item

import net.minecraft.block.BlockState
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.registry.RegistryKey
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.yukulab.robandpeace.DelegatedLogger
import net.yukulab.robandpeace.MOD_ID
import net.yukulab.robandpeace.RobAndPeace
import net.yukulab.robandpeace.extension.floor
import net.yukulab.robandpeace.extension.getCache
import net.yukulab.robandpeace.item.component.RapComponents
import net.yukulab.robandpeace.util.PortalData
import net.yukulab.robandpeace.util.PortalUtil
import qouteall.imm_ptl.core.api.PortalAPI
import qouteall.imm_ptl.core.portal.Portal
import qouteall.imm_ptl.core.portal.global_portals.GlobalPortalStorage

class PortalHoopItem : Item(Settings()) {
    companion object {
        private val logger by DelegatedLogger()
        private val SEARCH_MAX = 10

        const val SUFFIX_REMOVE_MODE: String = "_remove"
        val KEY_REMOVE_MODE: Identifier = Identifier.of(MOD_ID, "remove")

        val MSG_AIR_NOT_FOUND: Identifier = Identifier.of(MOD_ID, "air_not_found")
    }

    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        // If client side
        if (context.world.isClient || context.world !is ServerWorld) return super.useOnBlock(context)

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
        if (RobAndPeace.isDebugMode) logger.info("Side:${context.side}, Hitpos:${context.hitPos}, blockPos:${context.blockPos}, CeiledHitpos:${context.hitPos.floor()}")

        val portalOriginPos: BlockPos = context.blockPos.offset(context.side)

        val exploreDirection: Direction = context.side.opposite

        val cacheEndPos = context.blockPos.offset(exploreDirection, SEARCH_MAX)
        // Cache world
        val worldCache = context.world.getCache(portalOriginPos, cacheEndPos, Direction.UP)
        if (RobAndPeace.isDebugMode) logger.info("World cached! from:$portalOriginPos -> to:$cacheEndPos")

        val currentPos = portalOriginPos.mutableCopy()
        for (i in 0..SEARCH_MAX) {
            if (RobAndPeace.isDebugMode) logger.info("Place loop in $i")

            // Move to exploreDirection
            currentPos.move(exploreDirection)

            if (worldCache.isOutOfHeightLimit(currentPos)) {
                logger.error("Out of cache's height limit")
                return ActionResult.FAIL
            }

            // Get state
            val currentState = worldCache.getBlockState(currentPos)
            val calculatedIsAir = isAir(currentState)
            if (RobAndPeace.isDebugMode) logger.info("Pos: $currentPos, BlockId: ${currentState.block.name}, IsAir:${currentState.isAir}, =AIR:$calculatedIsAir")

            // If under block is air
            if (calculatedIsAir) {
                if (RobAndPeace.isDebugMode) logger.info("Checking upper block....")
                val extCurrentState = worldCache.getBlockState(currentPos.up())
                val extCalculatedIsAir = isAir(extCurrentState)

                if (extCalculatedIsAir) {
                    // If upper block is air too
                    if (RobAndPeace.isDebugMode) logger.info("This block is equal as air block!")
                    if (RobAndPeace.isDebugMode) logger.info("Found it! pos: $currentPos")

                    // Portal placement
                    val actualOriginVec = portalOriginPos.toCenterPos().offset(exploreDirection, 0.3).add(0.0, 0.5, 0.0)
                    val actualDestVec = currentPos.toCenterPos().offset(context.side, 0.3).add(0.0, 0.5, 0.0)
                    if (RobAndPeace.isDebugMode) logger.info("Actual destination -> $actualDestVec")

                    val destDimKey: RegistryKey<World> = (context.player ?: error("Failed to get player dimension registrykey")).world.registryKey

                    val portalData: PortalData = PortalUtil.createPortal(
                        context.world as ServerWorld,
                        actualOriginVec,
                        context.side,
                        destDimKey,
                        actualDestVec,
                    )

                    heldStack.set(RapComponents.PORTAL_ID_ORIGIN, portalData.origin.id)
                    heldStack.set(RapComponents.PORTAL_ID_DESTINATION, portalData.destination.id)

                    return ActionResult.SUCCESS
                } else {
                    // If upper block wasn't air
                    if (RobAndPeace.isDebugMode) logger.info("Upper block is not air. continue...")
                    continue
                }
            }
        }

        if (RobAndPeace.isDebugMode) logger.info("Couldn't find air space...")

        return ActionResult.PASS
    }

    private fun isAir(state: BlockState): Boolean = state.isAir

    private fun onRemovePortal(world: World, user: PlayerEntity, heldStack: ItemStack): TypedActionResult<ItemStack> {
        // Get all portals
        val portals: MutableList<Portal> = GlobalPortalStorage.getGlobalPortals(world).toMutableList()

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
}
