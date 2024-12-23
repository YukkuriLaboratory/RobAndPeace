package net.yukulab.robandpeace.mixin.spiderwalker;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = Entity.class)
public abstract class EntityMixin {
	@Shadow
	private boolean onGround;
	@Unique
	boolean realOnGround;
	@Inject(method = "adjustMovementForCollisions", at = @At("HEAD"))
	void allowJumpStepHead(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
		this.realOnGround = this.onGround;
		this.onGround = true;
	}
	@Inject(method = "adjustMovementForCollisions", at = @At("TAIL"))
	void allowJumpStepTail(Vec3d movement, CallbackInfoReturnable<Vec3d> cir) {
		this.onGround = this.realOnGround;
	}
}
