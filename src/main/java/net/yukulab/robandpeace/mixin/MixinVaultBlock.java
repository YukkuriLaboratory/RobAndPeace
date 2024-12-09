package net.yukulab.robandpeace.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.VaultBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.yukulab.robandpeace.item.PickingToolItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VaultBlock.class)
public abstract class MixinVaultBlock {
    @Inject(
            method = "onUseWithItem",
            at = @At("HEAD"),
            cancellable = true
    )
    private void passToDefaultBlockInteractionIfPlayerHasPickingTool(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ItemActionResult> cir) {
        if (stack.getItem() instanceof PickingToolItem) {
            cir.setReturnValue(ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION);
        }
    }
}
