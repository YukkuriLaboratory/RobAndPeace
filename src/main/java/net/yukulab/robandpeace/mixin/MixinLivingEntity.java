package net.yukulab.robandpeace.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.yukulab.robandpeace.config.RapConfigs;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Unique
    long robandpeace$stealCooldown = 0;

    @Shadow
    protected abstract void dropLoot(DamageSource damageSource, boolean causedByPlayer);

    @Shadow
    protected abstract void dropXp(@Nullable Entity attacker);

    @Shadow
    protected int playerHitTimer;

    /**
     * disableAttackingInCoolTimeがtrueの場合、クールダウン中は攻撃時のノックバックや耐久消費が発生しないようにする
     */
    @Inject(
            method = "damage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void checkStealCooldownIfAttackingDisabled(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() instanceof PlayerEntity) {
            if (robandpeace$stealCooldown > 0 && RapConfigs.getServerConfig().disableAttackingInCoolTime) {
                cir.setReturnValue(false);
            }
        }
    }

    /**
     * stealCooldownが0以上の場合、クールダウン中はダメージを受けないようにする(disableAttackingInCoolTimeがfalseの場合)
     */
    @WrapWithCondition(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V"
            )
    )
    private boolean checkStealCooldown(LivingEntity instance, DamageSource source, float amount) {
        return !(source.getAttacker() instanceof PlayerEntity) || robandpeace$stealCooldown <= 0;
    }

    @WrapWithCondition(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;sendEntityDamage(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/damage/DamageSource;)V"
            )
    )
    private boolean disableDamageSendingIfPlayerAttacked(World instance, Entity entity, DamageSource damageSource) {
        return !(damageSource.getAttacker() instanceof PlayerEntity);
    }

    @WrapWithCondition(
            method = "damage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;playHurtSound(Lnet/minecraft/entity/damage/DamageSource;)V"
            )
    )
    private boolean disableHurtSoundIfPlayerAttacked(LivingEntity instance, DamageSource damageSource) {
        return !(damageSource.getAttacker() instanceof PlayerEntity);
    }

    @Inject(
            method = "applyDamage",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;applyArmorToDamage(Lnet/minecraft/entity/damage/DamageSource;F)F"
            ),
            cancellable = true
    )
    private void replacePlayerAttackBehavior(DamageSource source, float amount, CallbackInfo ci) {
        if (source.getAttacker() instanceof PlayerEntity player) {
            var entity = (LivingEntity) (Object) this;
            var chance = switch (entity) {
                // bosses
                case ElderGuardianEntity ignored -> RapConfigs.getServerConfig().stealChances.boss;
                case PiglinBruteEntity ignored -> RapConfigs.getServerConfig().stealChances.boss;
                case EvokerEntity ignored -> RapConfigs.getServerConfig().stealChances.boss;
                case WardenEntity ignored -> RapConfigs.getServerConfig().stealChances.boss;
                case WitherEntity ignored -> RapConfigs.getServerConfig().stealChances.boss;
                case EnderDragonEntity ignored -> RapConfigs.getServerConfig().stealChances.boss;
                // normal mobs
                case VillagerEntity ignored -> 100;
                case HostileEntity ignored -> RapConfigs.getServerConfig().stealChances.hostile;
                default -> RapConfigs.getServerConfig().stealChances.friendly;
            };
            var rand = entity.getRandom().nextInt(100);
            if (rand >= chance) {
                robandpeace$stealCooldown = RapConfigs.getServerConfig().stealCoolTime.onFailure;
                ci.cancel();
                return;
            }
            dropLoot(source, true);
            // playerHitTimerを0以上にしないとxpが落ちない
            playerHitTimer = 100;
            dropXp(player);
            robandpeace$stealCooldown = RapConfigs.getServerConfig().stealCoolTime.onSuccess;
            entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_CHICKEN_EGG, entity.getSoundCategory(), 1.0F, 1.1F);
            if (entity instanceof VillagerEntity) {
                // TODO Spawn angry iron golem
            }
            ci.cancel();
        }
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void decreaseCooldown(CallbackInfo ci) {
        if (robandpeace$stealCooldown > 0) {
            robandpeace$stealCooldown--;
        }
    }
}
