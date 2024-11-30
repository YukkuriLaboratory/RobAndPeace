package net.yukulab.robandpeace.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.block.vault.VaultConfig;
import net.minecraft.block.vault.VaultServerData;
import net.minecraft.block.vault.VaultSharedData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(VaultBlockEntity.Server.class)
public interface AccessorVaultBlockEntity$Server {
    @Invoker
    static void invokeUnlock(ServerWorld world, BlockState state, BlockPos pos, VaultConfig config, VaultServerData serverData, VaultSharedData sharedData, List<ItemStack> itemsToEject) {
        throw new AssertionError();
    }

    @Invoker
    static List<ItemStack> invokeGenerateLoot(ServerWorld world, VaultConfig config, BlockPos pos, PlayerEntity player) {
        throw new AssertionError();
    }

    @Invoker
    static void invokePlayFailedUnlockSound(ServerWorld world, VaultServerData serverData, BlockPos pos, SoundEvent sound) {
        throw new AssertionError();
    }
}
