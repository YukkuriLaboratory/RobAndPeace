package net.yukulab.robandpeace.mixin.client.spiderwalker;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientPlayerEntity.class, priority = 999)
public abstract class MixinClientPlayerEntity extends PlayerEntity {

    public MixinClientPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }
    @Shadow
    protected abstract boolean canSprint();
    @Shadow public Input input;

    /**
     * Removes sprinting loss on collision, strafing, and more.
     */
    @ModifyReceiver(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V"))
    private ClientPlayerEntity removeSprintingLogic(ClientPlayerEntity clientPlayerEntity, boolean sprinting) {
        if (sprinting || !this.canSprint() || !(this.input.movementForward > 1.0E-5F)){
            this.setSprinting(sprinting);
        }
        return clientPlayerEntity;
    }
}
