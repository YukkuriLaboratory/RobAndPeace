package net.yukulab.robandpeace.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;
import net.yukulab.robandpeace.item.RapItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderDragonEntity.class)
public abstract class MixinEnderDragonEntity {
    @Inject(
            method = "kill",
            at = @At("HEAD")
    )
    private void dropHiddenTreasure(CallbackInfo ci) {
        robandpeace$dropHiddenTreasureItem();
    }

    @Inject(
            method = "updatePostDeath",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/boss/dragon/EnderDragonEntity;remove(Lnet/minecraft/entity/Entity$RemovalReason;)V"
            )
    )
    private void dropHiddenTreasure2(CallbackInfo ci) {
        robandpeace$dropHiddenTreasureItem();
    }

    @Unique
    private void robandpeace$dropHiddenTreasureItem() {
        var dragon = (EnderDragonEntity) (Object) this;
        var itemStack = RapItems.INSTANCE.getHIDDEN_TREASURE().getDefaultStack();
        var origin = dragon.getWorld().getTopPosition(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, EndPortalFeature.offsetOrigin(dragon.getFightOrigin())).up(3);
        var itemEntity = new ItemEntity(dragon.getWorld(), origin.getX() + 3, origin.getY(), origin.getZ() + 3, itemStack);
        itemEntity.setToDefaultPickupDelay();
        dragon.getWorld().spawnEntity(itemEntity);
    }
}
