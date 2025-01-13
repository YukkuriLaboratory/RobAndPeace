package net.yukulab.robandpeace.mixin.client.spiderwalker;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.yukulab.robandpeace.RobAndPeace;
import net.yukulab.robandpeace.item.RapItems;
import net.yukulab.robandpeace.network.payload.PlayerMovementPayload;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = ClientPlayerEntity.class, priority = 999)
public abstract class MixinClientPlayerEntity extends PlayerEntity {
    public MixinClientPlayerEntity(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Unique
    private static final Logger logger = LogUtils.getLogger();

    @Shadow
    protected abstract boolean canSprint();

    @Shadow
    public Input input;

    @Shadow
    private boolean inSneakingPose;
    @Unique
    private PlayerMovementPayload sentPayload = null;

    @Inject(
            method = "tick",
            at = @At(value = "HEAD")
    )
    private void onTick(CallbackInfo ci) {
        // First synchronization
        if (sentPayload == null) {
            sentPayload = new PlayerMovementPayload(getUuid(), input.hasForwardMovement(), input.movementForward, input.jumping, input.sneaking);
            logger.info("First player movement syncing...");
            ClientPlayNetworking.send(sentPayload);
            logger.info("First player movement sync was completed.");
            return;
        }

        boolean isDirty = (sentPayload.getHasForwardMovement() != input.hasForwardMovement()) ||
                (sentPayload.getMovementForward() != input.movementForward) ||
                (sentPayload.isJumping() != input.jumping) ||
                (sentPayload.isSneaking() != input.sneaking);
        if (isDirty) {
            sentPayload = new PlayerMovementPayload(getUuid(), input.hasForwardMovement(), input.movementForward, input.jumping, input.sneaking);
            ClientPlayNetworking.send(sentPayload);
        }
    }

    /**
     * Removes sprinting loss on collision, strafing, and more.
     */
    @ModifyReceiver(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;setSprinting(Z)V"))
    private ClientPlayerEntity removeSprintingLogic(ClientPlayerEntity clientPlayerEntity, boolean sprinting) {
        var config = clientPlayerEntity.robandpeace$getServerConfigSupplier().get();
        boolean sidewaysSprint = config.spiderWalkerSettings.walking.sidewaysSprint;
        boolean backwardsSprint = config.spiderWalkerSettings.walking.backwardsSprint;
        if (sprinting ||
                !this.canSprint() ||
                !(this.input.movementForward > 1.0E-5F ||
                        (Math.abs(this.input.movementSideways) > 1.0E-5F &&
                                this.input.movementForward > -1.0E-5F && sidewaysSprint) ||
                        (this.input.movementForward < -1.0E-5F && backwardsSprint))) {
            this.setSprinting(sprinting);
        }
        return clientPlayerEntity;
    }

    @Inject(method = "tickMovement", at = @At("RETURN"))
    private void fixSneakingWhenClimbing(CallbackInfo ci) {
        if(isClimbing() && canClimbing()) {
            if (RobAndPeace.getPlayerMovementStatus(getUuid()).isJumping() && getWorld().getBlockState(getBlockPos().up(2)).getBlock() != Blocks.AIR) {
                logger.info("Sneak info: {}", input.sneaking);
                setSneaking(input.sneaking);
                inSneakingPose = input.sneaking;
            }
        }
    }

    @Unique
    private boolean canClimbing() {
        return getStackInHand(Hand.MAIN_HAND).getItem() == RapItems.INSTANCE.getSPIDER_WALKER() ||
                getStackInHand(Hand.OFF_HAND).getItem() == RapItems.INSTANCE.getSPIDER_WALKER();
    }
}
