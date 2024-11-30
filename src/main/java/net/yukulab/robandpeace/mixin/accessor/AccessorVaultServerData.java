package net.yukulab.robandpeace.mixin.accessor;

import net.minecraft.block.vault.VaultServerData;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(VaultServerData.class)
public interface AccessorVaultServerData {
    @Invoker
    boolean invokeHasRewardedPlayer(PlayerEntity player);
}
