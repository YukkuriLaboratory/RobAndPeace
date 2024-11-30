package net.yukulab.robandpeace.item

import net.minecraft.block.BlockState
import net.minecraft.block.entity.VaultBlockEntity
import net.minecraft.block.vault.VaultConfig
import net.minecraft.block.vault.VaultServerData
import net.minecraft.block.vault.VaultSharedData
import net.minecraft.entity.EntityStatuses
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.yukulab.robandpeace.item.component.RapComponents
import net.yukulab.robandpeace.mixin.accessor.`AccessorVaultBlockEntity$Server`
import net.yukulab.robandpeace.mixin.accessor.AccessorVaultServerData
import net.yukulab.robandpeace.mixin.accessor.AccessorVaultSharedData

class PickingToolItem(pickingChange: Int) : Item(Settings().component(RapComponents.PICKING_CHANCE, pickingChange)) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val world = context.world
        val player = context.player
        val stack = context.stack
        val blockPos = context.blockPos
        val blockState = world.getBlockState(blockPos)
        val blockEntity = world.getBlockEntity(blockPos)
        if (blockEntity is VaultBlockEntity && world is ServerWorld && player != null) {
            val serverData = blockEntity.serverData
            if (serverData != null) {
                forceUnlockVault(
                    world,
                    blockPos,
                    blockState,
                    blockEntity.config,
                    serverData,
                    blockEntity.sharedData,
                    player,
                    stack,
                )
            }
        }
        return super.useOnBlock(context)
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
        }
        stack.decrementUnlessCreative(1, player)
        world.sendEntityStatus(player, EntityStatuses.BREAK_MAINHAND)
    }
}
