package net.yukulab.robandpeace.mixin;

import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.yukulab.robandpeace.item.RapItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEntity.class)
public class MixinEnderDragonEntity {
    @Inject(
            method = "kill",
            at = @At("HEAD")
    )
    private void dropHiddenTreasure(CallbackInfo ci) {
        var dragon = (EnderDragonEntity) (Object) this;
        dragon.dropItem(RapItems.INSTANCE.getHIDDEN_TREASURE());
    }

    @Inject(
            method = "updatePostDeath",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;remove(Lnet/minecraft/entity/Entity$RemovalReason;)V"
            )
    )
    private void dropHiddenTreasure2(CallbackInfo ci) {
        var dragon = (EnderDragonEntity) (Object) this;
        dragon.dropItem(RapItems.INSTANCE.getHIDDEN_TREASURE());
    }
}
