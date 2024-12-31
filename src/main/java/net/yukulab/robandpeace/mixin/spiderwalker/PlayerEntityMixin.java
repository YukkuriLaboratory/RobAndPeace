package net.yukulab.robandpeace.mixin.spiderwalker;

import com.mojang.logging.LogUtils;
import net.minecraft.block.Blocks;
import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.yukulab.robandpeace.RobAndPeace;
import net.yukulab.robandpeace.VariablesKt;
import net.yukulab.robandpeace.config.RapServerConfig;
import net.yukulab.robandpeace.extension.RapConfigInjector;
import net.yukulab.robandpeace.item.RapItems;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;


@Mixin(value = PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private static final Logger logger = LogUtils.getLogger();

    @Shadow
    public abstract PlayerAbilities getAbilities();

    @Shadow
    public abstract HungerManager getHungerManager();

    @Shadow
    public abstract void incrementStat(Identifier stat);

    @Shadow
    public abstract void addExhaustion(float exhaustion);

    @Unique
    private static final Identifier SPRINTING_SPEED = Identifier.of(VariablesKt.MOD_ID, "sprinting_speed");

    @Unique
    private static final Identifier STEP_HEIGHT = Identifier.of(VariablesKt.MOD_ID, "step_height");

    @Unique
    private static final Identifier MOVEMENT_SPEED = Identifier.of(VariablesKt.MOD_ID, "movement_speed");

    @Unique
    private static final float DEFAULT_STEP_HEIGHT = 0.6f;

    @Shadow
    protected abstract float getOffGroundSpeed();

    @Unique
    private boolean canStartSprinting() {
        return !this.isSprinting() && this.isWalking() && this.canSprint() && !this.isUsingItem() && !this.hasStatusEffect(StatusEffects.BLINDNESS) && (!this.hasVehicle() || this.canVehicleSprint(this.getVehicle())) && !this.isFallFlying();
    }

    @Unique
    private boolean canVehicleSprint(Entity entity) {
        return entity.canSprintAsVehicle() && entity.isLogicalSideForUpdatingMovement();
    }

    @Unique
    private boolean isWalking() {
        var payload = RobAndPeace.getPlayerMovementStatus(getUuid());
        boolean hasForwardMovement = payload.getHasForwardMovement();
        float movementForward = payload.getMovementForward();
        return this.isSubmergedInWater() ? hasForwardMovement : (double) movementForward >= 0.8;
    }

    @Unique
    private boolean canSprint() {
        return this.hasVehicle() || (float) this.getHungerManager().getFoodLevel() > 6.0F || this.getAbilities().allowFlying;
    }

    /**
     * Executes a jump.
     * Edited s.t. variables can be configured
     */
//    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
//    public void jump(CallbackInfo ci) {
//        var config = this.robandpeace$getServerConfigSupplier().get();
//        float jumpStrength = config.spiderWalkerSettings.jumping.jumpStrength;
//        Vec3d velocity = getJumpVec3d(config, jumpStrength);
//        this.setVelocity(velocity);
//        this.velocityDirty = true;
//        this.incrementStat(Stats.JUMP);
//        if (this.isSprinting()) {
//            this.addExhaustion(0.2F);
//        } else {
//            this.addExhaustion(0.05F);
//        }
//        ci.cancel();
//    }

    @Unique
    private Vec3d getJumpVec3d(RapServerConfig config, float jumpStrength) {
        float jumpHorizontalVelocityMultiplier = config.spiderWalkerSettings.jumping.jumpHorizontalVelocityMultiplier;
        float sprintJumpHorizontalVelocityMultiplier = config.spiderWalkerSettings.jumping.sprintJumpHorizontalVelocityMultiplier;
        Vec3d velocity = this.getVelocity();
        float jumpVelocity = jumpStrength * this.getJumpVelocityMultiplier() + this.getJumpBoostVelocityModifier();
        velocity = new Vec3d(velocity.x, jumpVelocity, velocity.z);
        float f = this.getYaw() * 0.017453292F;
        if (this.isSprinting())
            velocity = velocity.add(-MathHelper.sin(f) * sprintJumpHorizontalVelocityMultiplier,
                    0.0, MathHelper.cos(f) * sprintJumpHorizontalVelocityMultiplier);
        else
            velocity = velocity.add(-MathHelper.sin(f) * jumpHorizontalVelocityMultiplier,
                    0.0, MathHelper.cos(f) * jumpHorizontalVelocityMultiplier);
        return velocity;
    }


    @Inject(method = "travel", at = @At("HEAD"))
    public void travelHead(Vec3d movementInput, CallbackInfo ci) {
        var config = this.robandpeace$getServerConfigSupplier().get();
        boolean alwaysSprint = config.spiderWalkerSettings.walking.alwaysSprint;
//        float defaultGenericMovementSpeed = config.spiderWalkerSettings.walking.defaultGenericMovementSpeed;
        float stepHeight = config.spiderWalkerSettings.walking.stepHeight;
        this.horizontalCollision = false;
        if (this.canStartSprinting())
            this.setSprinting(alwaysSprint || isSprinting());
        if (this.isSneaking()) {
            this.resetStepHeight();
        } else {
            this.setStepHeight(stepHeight);
        }
//        this.getAbilities().setWalkSpeed(defaultGenericMovementSpeed);
//        Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED))
//                .setBaseValue(this.getAbilities().getWalkSpeed());
//        EntityAttributeModifier modifier = new EntityAttributeModifier(MOVEMENT_SPEED, getAbilities().getWalkSpeed(), EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
//        var instance = getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
//        if (instance != null) {
//            instance.removeModifier(MOVEMENT_SPEED);
//            instance.addTemporaryModifier(modifier);
//        }
    }

    @Unique
    private void setStepHeight(float stepHeight) {
        var instance = this.getAttributes().getCustomInstance(EntityAttributes.GENERIC_STEP_HEIGHT);
        if (instance == null) {
            logger.warn("Failed to get step height attribute.");
            return;
        }

        EntityAttributeModifier modifier = new EntityAttributeModifier(STEP_HEIGHT, DEFAULT_STEP_HEIGHT-stepHeight, EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        instance.removeModifier(STEP_HEIGHT);
        instance.addTemporaryModifier(modifier);
    }

    @Unique
    private void resetStepHeight() {
        var instance = this.getAttributeInstance(EntityAttributes.GENERIC_STEP_HEIGHT);
        if(instance == null) {
            logger.warn("Failed to get step height attribute.");
            return;
        }
        instance.removeModifier(STEP_HEIGHT);
    }


    /**
     * Overrides the default friction behavior to add wall sliding.
     */
    @Override
    public Vec3d applyMovementInput(Vec3d movementInput, float slipperiness) {
        this.updateVelocity(this.getMovementSpeed(slipperiness), movementInput);

        var config = this.robandpeace$getServerConfigSupplier().get();
        boolean wallMovement = config.spiderWalkerSettings.wall.wallMovement;

        if (super.isClimbing())
            this.setVelocity(this.applyClimbingSpeed(this.getVelocity()));

        else if (wallMovement && !this.isSpectator())
            this.setVelocity(this.applyWallMovement(this.getVelocity()));

        this.move(MovementType.SELF, this.getVelocity());

        return this.getVelocity();
    }

    @Unique
    private float getMovementSpeed(float slipperiness) {
        return (this.isOnGround()) ? this.getMovementSpeed() * (0.21600002F / (slipperiness * slipperiness * slipperiness)) : this.getOffGroundSpeed();
    }

    @Unique
    private boolean isWalling = false;

    /**
     * Overrides the default friction behavior to add wall sliding, wall running, wall climbing, and wall sticking.
     */
    @Unique
    private Vec3d applyWallMovement(Vec3d motion) {
        if (this.isOnGround()) {
            this.isWalling = false;
            return motion;
        }

        if (!canClimbing()) return motion;

        RapConfigInjector injector = this;
        var config = injector.robandpeace$getServerConfigSupplier().get();
        float wallDistance = config.spiderWalkerSettings.wall.wallDistance;
        float minimumYawToJump = config.spiderWalkerSettings.wall.minimumYawToJump;
        boolean jumpOnLeavingWall = config.spiderWalkerSettings.wall.jumpOnLeavingWall;
        float wallJumpVelocityMultiplier = config.spiderWalkerSettings.wall.wallJumpVelocityMultiplier;
        float wallJumpHeight = config.spiderWalkerSettings.wall.wallJumpHeight;
        boolean wallRunning = config.spiderWalkerSettings.wall.wallRunning;
        float yawToRun = config.spiderWalkerSettings.wall.yawToRun;
        float minimumWallRunSpeed = config.spiderWalkerSettings.wall.minimumWallRunSpeed;
        float wallRunSlidingSpeed = config.spiderWalkerSettings.wall.wallRunSlidingSpeed;
        float wallRunSpeedBonus = config.spiderWalkerSettings.wall.wallRunSpeedBonus;
        boolean wallClimbing = config.spiderWalkerSettings.wall.wallClimbing;
        float pitchToClimb = config.spiderWalkerSettings.wall.pitchToClimb;
        float climbingSpeed = config.spiderWalkerSettings.wall.climbingSpeed;
        boolean wallSticking = config.spiderWalkerSettings.wall.wallSticking;
        boolean wallSliding = config.spiderWalkerSettings.wall.wallSliding;
        float slidingSpeed = config.spiderWalkerSettings.wall.slidingSpeed;
        boolean stickyMovement = config.spiderWalkerSettings.wall.stickyMovement;
        boolean wallJumping = config.spiderWalkerSettings.wall.wallJumping;

        var payload = RobAndPeace.getPlayerMovementStatus(getUuid());
        boolean hasForwardMovement = payload.getHasForwardMovement();
        boolean inputJumping = payload.isJumping();

        BlockPos blockPos = this.getBlockPos().up();
        World world = this.getWorld();

        double dx = (double) blockPos.getX() + 0.5 - this.getX();
        double dz = (double) blockPos.getZ() + 0.5 - this.getZ();
        double threshold = (double) (this.getWidth() / 2.0F) - 0.1F - wallDistance - 1.0E-7;

        boolean east = (world.isDirectionSolid(blockPos.east(), this, Direction.WEST) && -dx > threshold);
        boolean west = (world.isDirectionSolid(blockPos.west(), this, Direction.EAST) && dx > threshold);
        boolean north = (world.isDirectionSolid(blockPos.north(), this, Direction.SOUTH) && dz > threshold);
        boolean south = (world.isDirectionSolid(blockPos.south(), this, Direction.NORTH) && -dz > threshold);
        int wallsTouching = (east ? 1 : 0) + (west ? 1 : 0) + (north ? 1 : 0) + (south ? 1 : 0);

        if (wallsTouching == 0 && isWalling) { //Start only using head, continue with feet.
            blockPos = this.getBlockPos();
            east = (world.isDirectionSolid(blockPos.east(), this, Direction.WEST) && -dx > threshold);
            west = (world.isDirectionSolid(blockPos.west(), this, Direction.EAST) && dx > threshold);
            north = (world.isDirectionSolid(blockPos.north(), this, Direction.SOUTH) && dz > threshold);
            south = (world.isDirectionSolid(blockPos.south(), this, Direction.NORTH) && -dz > threshold);
            wallsTouching = (east ? 1 : 0) + (west ? 1 : 0) + (north ? 1 : 0) + (south ? 1 : 0);
        }

        float yaw = this.getYaw();
        float pitch = this.getPitch() * -1;
        yaw += (90.0F * ((east ? 1 : 0) - (west ? 1 : 0) + (north ? (east ? 2 : -2) : 0)) / wallsTouching);
        yaw = MathHelper.wrapDegrees(yaw);
        yaw = Math.abs(yaw);

        double motionX = motion.x;
        double motionZ = motion.z;
        double motionY = motion.y;

        if (this.isWalling && ((wallJumping && !inputJumping && yaw > minimumYawToJump) || (jumpOnLeavingWall && wallsTouching == 0))) {// Do a wall jump
            float f = this.getYaw() * 0.017453292F;
            motionX += -MathHelper.sin(f) * wallJumpVelocityMultiplier;
            motionZ += MathHelper.cos(f) * wallJumpVelocityMultiplier;
            motionY += wallJumpHeight * this.getJumpVelocityMultiplier() + this.getJumpBoostVelocityModifier();
            this.isWalling = false;
            this.slidingPos = Optional.of(this.getBlockPos());
            this.onLanding();
            return new Vec3d(motionX, motionY, motionZ);
        }

        if (wallsTouching == 0 || !inputJumping || (yaw > 90 && !this.isWalling)) { // Stop all wall movement
            this.isWalling = false;
            return motion;
        }


        this.isWalling = true;
        if (wallRunning && hasForwardMovement && yaw > yawToRun && (Math.abs(motionX) > minimumWallRunSpeed || Math.abs(motionZ) > minimumWallRunSpeed)) { // Wall Running
            motionY = Math.max(motion.y, -wallRunSlidingSpeed);
            motionX = motion.x * (1 + wallRunSpeedBonus);
            motionZ = motion.z * (1 + wallRunSpeedBonus);
        } else if (wallClimbing && yaw < 90 && hasForwardMovement) { // Wall Climbing
            motionY = climbingSpeed;
            motionX = MathHelper.clamp(motionX, -climbingSpeed, climbingSpeed);
            motionZ = MathHelper.clamp(motionZ, -climbingSpeed, climbingSpeed);
        } else if (wallSticking && motionY < 0.0 && this.isHoldingOntoLadder()) { //Shifting
            motionY = 0.0;
            motionX = MathHelper.clamp(motionX, -climbingSpeed, climbingSpeed);
            motionZ = MathHelper.clamp(motionZ, -climbingSpeed, climbingSpeed);
        } else if (wallSliding) { // Wall Sliding
            motionY = Math.max(motion.y, -slidingSpeed);
            motionX = MathHelper.clamp(motionX, -climbingSpeed, climbingSpeed);
            motionZ = MathHelper.clamp(motionZ, -climbingSpeed, climbingSpeed);
        } else {
            this.isWalling = false;
            return motion;
        }

        if (stickyMovement) { // Disable falling off the wall accidentally
            motionX *= ((north ? 1 : 0) + (south ? 1 : 0));
            motionZ *= ((east ? 1 : 0) + (west ? 1 : 0));
        }
        this.slidingPos = Optional.of(this.getBlockPos());
        this.onLanding();
        return new Vec3d(motionX, motionY, motionZ);
    }

    @Unique
    private Vec3d applyClimbingSpeed(Vec3d motion) {
        this.onLanding();
        float climbingSpeed = 0.15000000596046448F;
        double d = MathHelper.clamp(motion.x, -climbingSpeed, climbingSpeed);
        double e = MathHelper.clamp(motion.z, -climbingSpeed, climbingSpeed);
        double g = Math.max(motion.y, -climbingSpeed);
        if ((this.horizontalCollision || this.jumping) && (this.isClimbing() || this.getBlockStateAtPos().isOf(Blocks.POWDER_SNOW) && PowderSnowBlock.canWalkOnPowderSnow(this))) {
            g = 0.2;
        } else if (g < 0.0 && !this.getBlockStateAtPos().isOf(Blocks.SCAFFOLDING) && this.isHoldingOntoLadder()) {
            g = 0.0;
        }

        motion = new Vec3d(d, g, e);
        return motion;
    }

    /**
     * Used in fall damage calculations, probably currently broken
     */
    @Unique
    private Optional<BlockPos> slidingPos = Optional.empty();

    @Override
    public Optional<BlockPos> getClimbingPos() {
        if (this.slidingPos.isEmpty() || !canClimbing())
            return super.getClimbingPos();
        return this.slidingPos;
    }

    @Override
    public boolean isClimbing() {
        return (super.isClimbing() || this.isWalling) && canClimbing();
    }

    @Unique
    private boolean canClimbing() {
        return getStackInHand(Hand.MAIN_HAND).getItem() == RapItems.INSTANCE.getSPIDER_WALKER() ||
                getStackInHand(Hand.OFF_HAND).getItem() == RapItems.INSTANCE.getSPIDER_WALKER();
    }

}
