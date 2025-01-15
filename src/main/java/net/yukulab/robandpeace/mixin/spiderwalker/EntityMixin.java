package net.yukulab.robandpeace.mixin.spiderwalker;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.util.math.Vec3d;
import net.yukulab.robandpeace.extension.RapConfigInjector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = Entity.class)
public abstract class EntityMixin {
    @Shadow
    private boolean onGround;
    @Unique
    boolean realOnGround;

    @Inject(method = "adjustMovementForCollisions", at = @At("HEAD"))
    void allowJumpStepHead(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
        if (this instanceof RapConfigInjector injector) {
            var config = injector.robandpeace$getServerConfigSupplier().get();
            boolean smoothJumps = config.spiderWalkerSettings.jumping.smoothJumps;
            this.realOnGround = this.onGround;
            this.onGround |= smoothJumps;
        }
    }

    @Inject(method = "adjustMovementForCollisions", at = @At("TAIL"))
    void allowJumpStepTail(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
        this.onGround = this.realOnGround;
    }

    /**
     * {@link PlayerEntityMixin#expandDimensionsWhenJumping(CallbackInfo)}や{@link PlayerEntityMixin#applyWallMovement(Vec3d)}の中にあるcalculateDimensionsにて発動する
     * {@link PlayerEntityMixin#keepStandingDimensionsIfPlayerClimbingOrJumpingWithCrouching(EntityPose, CallbackInfoReturnable)}によって伸びた身長を元に戻すための処理
     */
    @Inject(method = "setOnGround*", at = @At("HEAD"))
    private void recalculateDimensions(CallbackInfo ci) {
        var entity = (Entity) (Object) this;
        entity.calculateDimensions();
    }
}
