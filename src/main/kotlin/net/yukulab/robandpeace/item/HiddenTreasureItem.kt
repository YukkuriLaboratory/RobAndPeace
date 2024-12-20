package net.yukulab.robandpeace.item

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.yukulab.robandpeace.DelegatedLogger
import net.yukulab.robandpeace.entity.HiddenTreasureEntity
import net.yukulab.robandpeace.entity.RapEntityType

class HiddenTreasureItem : Item(Settings()) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val heldStack = user.getStackInHand(hand)
        if (world.isClient) return TypedActionResult.pass(heldStack)
        user.sendMessage(Text.of("Hidden Treasure was used!")) // TODO remove this on merge

        // if (this.ticksSinceDeath >= 180 && this.ticksSinceDeath <= 200) {
        // val f: Float = (user.random.nextFloat() - 0.5f) * 8.0f
        // val g: Float = (user.random.nextFloat() - 0.5f) * 4.0f
        // val h: Float = (user.random.nextFloat() - 0.5f) * 8.0f
        // world.addParticle(
        //     ParticleTypes.EXPLOSION_EMITTER,
        //     user.x + f.toDouble(),
        //     user.y + 2.0 + (g.toDouble()),
        //     user.z + h.toDouble(),
        //     0.0,
        //     0.0,
        //     0.0,
        // )
        // }
        val entity: HiddenTreasureEntity = RapEntityType.HIDDEN_TREASURE_ENTITY.create(world) ?: error("Failed to create hidden treasure entity")
        entity.setPosition(user.syncedPos.add(0.0, 1.0, 0.0))
        logger.info("User Pos: {}", user.pos)
        if (world.spawnEntity(entity)) {
            logger.info("Hidden treasure entity was spawned!")
        } else {
            logger.warn("Failed to spawn hidden treasure entity.")
        }

        user.setStackInHand(hand, ItemStack.EMPTY)
        return TypedActionResult.consume(heldStack)
    }

    companion object {
        private val logger by DelegatedLogger()
    }
}
