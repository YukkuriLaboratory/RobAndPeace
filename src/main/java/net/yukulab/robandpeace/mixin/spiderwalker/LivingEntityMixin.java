package net.yukulab.robandpeace.mixin.spiderwalker;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.yukulab.robandpeace.RobAndPeace;
import net.yukulab.robandpeace.extension.RapConfigInjector;
import net.yukulab.robandpeace.item.RapItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract void remove(RemovalReason reason);

    @Shadow
    protected abstract void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition);

    @Shadow
    public abstract ItemStack getStackInHand(Hand hand);

    @Shadow
    private Optional<BlockPos> climbingPos;

    /**
     * Used in fall damage calculations, probably currently broken
     */
    @Unique
    private Optional<BlockPos> slidingPos = Optional.empty();

    @Inject(method = "getClimbingPos", at = @At("HEAD"), cancellable = true)
    public void getClimbingPosHead(CallbackInfoReturnable<Optional<BlockPos>> cir) {
        if (this.slidingPos.isEmpty() || !canClimbing())
            return;
        cir.setReturnValue(this.slidingPos);
    }

    @Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
    public void isClimbingHead(CallbackInfoReturnable<Boolean> cir) {
        if (canClimbing()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isClimbing", at = @At("TAIL"), cancellable = true)
    public void isClimbingOnGrowBerries(CallbackInfoReturnable<Boolean> cir) {
        BlockPos blockPos = getBlockPos();

        if (RobAndPeace.getPlayerMovementStatus(getUuid()).isJumping() && getWorld().getBlockState(blockPos.up(2)).getBlock() != Blocks.AIR) {
            climbingPos = Optional.of(blockPos);
            cir.setReturnValue(true);
        }
    }

    @Unique
    private boolean canClimbing() {
        return getStackInHand(Hand.MAIN_HAND).getItem() == RapItems.INSTANCE.getSPIDER_WALKER() ||
                getStackInHand(Hand.OFF_HAND).getItem() == RapItems.INSTANCE.getSPIDER_WALKER();
    }
}
