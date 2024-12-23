package net.yukulab.robandpeace.item

import net.minecraft.entity.Entity
import net.minecraft.entity.boss.dragon.EnderDragonEntity
import net.minecraft.entity.boss.dragon.EnderDragonFight
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
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
        if (world.isClient || world !is ServerWorld) return TypedActionResult.pass(heldStack)
        user.sendMessage(Text.of("Hidden Treasure was used!")) // TODO remove this on merge

        val dragonFight: EnderDragonFight = world.enderDragonFight ?: error("Failed to get ender dragon fight")
        val dragonEntity: Entity = world.getEntity(dragonFight.dragonUuid) ?: error("Failed to get ender dragon entity")
        if (dragonEntity !is EnderDragonEntity) error("Entity type not matched as EnderDragonEntity, actual: ${dragonEntity.type.name}")

        dragonEntity.kill()
        dragonFight.dragonKilled(dragonEntity)

        val entity: HiddenTreasureEntity = RapEntityType.HIDDEN_TREASURE_ENTITY.create(world) ?: error("Failed to create hidden treasure entity")
        entity.setPosition(user.syncedPos.add(0.0, 1.0, 0.0))
        logger.info("User Pos: {}", user.pos)
        if (world.spawnEntity(entity)) {
            logger.info("Hidden treasure entity was spawned!")
        } else {
            logger.error("Failed to spawn hidden treasure entity.")
            entity.kill()
            return TypedActionResult.fail(heldStack)
        }

        user.setStackInHand(hand, ItemStack.EMPTY)
        return TypedActionResult.consume(heldStack)
    }

    companion object {
        private val logger by DelegatedLogger()
    }
}
