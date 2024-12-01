package net.yukulab.robandpeace.item

import net.minecraft.block.BlockState
import net.minecraft.block.entity.VaultBlockEntity
import net.minecraft.block.vault.VaultConfig
import net.minecraft.block.vault.VaultServerData
import net.minecraft.block.vault.VaultSharedData
import net.minecraft.entity.EntityStatuses
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.UseAction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.yukulab.robandpeace.item.component.RapComponents
import net.yukulab.robandpeace.mixin.accessor.`AccessorVaultBlockEntity$Server`
import net.yukulab.robandpeace.mixin.accessor.AccessorVaultServerData
import net.yukulab.robandpeace.mixin.accessor.AccessorVaultSharedData

class PickingToolItem(pickingChange: Int) : Item(Settings().component(RapComponents.PICKING_CHANCE, pickingChange)) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val player = context.player
        val stack = context.stack
        if (player?.activeItem == stack) {
            return ActionResult.PASS
        }
        val blockPos = context.blockPos
        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is VaultBlockEntity && world is ServerWorld && player != null) {
            player.setCurrentHand(context.hand)
        }
        return ActionResult.PASS
    }

    override fun getMaxUseTime(stack: ItemStack?, user: LivingEntity?): Int {
        val chance = stack?.get(RapComponents.PICKING_CHANCE) ?: 1
        return (1.6 * chance).toInt()
    }

    override fun getUseAction(stack: ItemStack?): UseAction = UseAction.BOW

    override fun usageTick(world: World?, user: LivingEntity?, stack: ItemStack?, remainingUseTicks: Int) {
        if (world is ServerWorld && user is PlayerEntity) {
            val result = user.raycast(user.getAttributeValue(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE), 0.0f, false)
            if (result is BlockHitResult) {
                val blockEntity = world.getBlockEntity(result.blockPos)
                if (blockEntity !is VaultBlockEntity) {
                    user.stopUsingItem()
                } else if (remainingUseTicks % 4 == 0 && world.random.nextInt(4) < 2) {
                    world.playSound(null, user.blockPos, SoundEvents.BLOCK_CHAIN_BREAK, user.soundCategory, 1.0f, 2f)
                }
            }
        }
    }

    override fun finishUsing(stack: ItemStack, world: World?, user: LivingEntity?): ItemStack {
        if (world is ServerWorld && user is PlayerEntity) {
            val result = user.raycast(user.getAttributeValue(EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE), 0.0f, false)
            if (result is BlockHitResult) {
                val state = world.getBlockState(result.blockPos)
                val blockEntity = world.getBlockEntity(result.blockPos)
                if (blockEntity is VaultBlockEntity) {
                    val serverData = blockEntity.serverData
                    if (serverData != null) {
                        forceUnlockVault(world, result.blockPos, state, blockEntity.config, serverData, blockEntity.sharedData, user, stack)
                    }
                }
            }
        }
        return stack
    }

    /**
     * [net.minecraft.block.entity.VaultBlockEntity.Server.tryUnlock]
     */
    private fun forceUnlockVault(world: ServerWorld, pos: BlockPos, state: BlockState, config: VaultConfig, serverData: VaultServerData, sharedData: VaultSharedData, player: PlayerEntity, stack: ItemStack) {
        if ((serverData as AccessorVaultServerData).invokeHasRewardedPlayer(player)) {
            `AccessorVaultBlockEntity$Server`.invokePlayFailedUnlockSound(
                world,
                serverData,
                pos,
                SoundEvents.BLOCK_VAULT_REJECT_REWARDED_PLAYER,
            )
            return
        }
        val chance = stack.get(RapComponents.PICKING_CHANCE) ?: 1
        val result = world.random.nextInt(100) + 1
        if (chance >= result) {
            val loots = `AccessorVaultBlockEntity$Server`.invokeGenerateLoot(world, config, pos, player)
            if (loots.isNotEmpty()) {
                player.incrementStat(Stats.USED.getOrCreateStat(this))
                `AccessorVaultBlockEntity$Server`.invokeUnlock(world, state, pos, config, serverData, sharedData, loots)
                serverData.markPlayerAsRewarded(player)
                (sharedData as AccessorVaultSharedData).invokeUpdateConnectedPlayers(
                    world,
                    pos,
                    serverData,
                    config,
                    config.deactivationRange,
                )
            }
        } else {
            stack.decrementUnlessCreative(1, player)
            world.sendEntityStatus(player, EntityStatuses.BREAK_MAINHAND)
        }
    }
}
