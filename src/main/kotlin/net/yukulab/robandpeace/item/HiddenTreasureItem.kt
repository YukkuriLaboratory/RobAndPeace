package net.yukulab.robandpeace.item

import net.minecraft.entity.SpawnReason
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.yukulab.robandpeace.entity.RapEntityType

class HiddenTreasureItem : Item(Settings()) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val heldStack = user.getStackInHand(hand)
        if (world.isClient || world !is ServerWorld) return TypedActionResult.pass(heldStack)

        RapEntityType.HIDDEN_TREASURE_ENTITY.spawn(world, { it.owner = user }, user.blockPos, SpawnReason.MOB_SUMMONED, true, true)
        user.getStackInHand(hand).decrement(1)
        return TypedActionResult.consume(heldStack)
    }
}
