package net.yukulab.robandpeace.mixin.accessor;

import net.minecraft.block.vault.VaultConfig;
import net.minecraft.block.vault.VaultServerData;
import net.minecraft.block.vault.VaultSharedData;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VaultSharedData.class)
public interface AccessorVaultSharedData {
    @Invoker
    void invokeUpdateConnectedPlayers(ServerWorld world, BlockPos pos, VaultServerData serverData, VaultConfig config, double radius);
}
