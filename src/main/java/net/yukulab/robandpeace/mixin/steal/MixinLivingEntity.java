package net.yukulab.robandpeace.mixin.steal;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.yukulab.robandpeace.config.RapConfigs;
import net.yukulab.robandpeace.config.RapServerConfig;
import net.yukulab.robandpeace.entity.RapEntityType;
import net.yukulab.robandpeace.extension.RapConfigInjector;
import net.yukulab.robandpeace.extension.StealCooldownHolder;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements StealCooldownHolder, RapConfigInjector {
    @Unique
    private static TrackedData<Long> ROBANDPEACE_STEAL_COOLDOWN;

    @Unique
    private Supplier<RapServerConfig> robandpeace$serverConfigSupplier = RapConfigs::getServerConfig;

    @Shadow
    protected abstract void dropLoot(DamageSource damageSource, boolean causedByPlayer);

    @Shadow
    protected abstract void dropXp(@Nullable Entity attacker);

    @Shadow
    protected int playerHitTimer;

    @Shadow
    @Final
    private static Logger LOGGER;

    @Inject(
            method = "<clinit>",
            at = @At("RETURN")
    )
    private static void registerDataTracker(CallbackInfo ci) {
        ROBANDPEACE_STEAL_COOLDOWN = DataTracker.registerData(LivingEntity.class, TrackedDataHandlerRegistry.LONG);
    }

    @Inject(
            method = "initDataTracker",
            at = @At("RETURN")
    )
    private void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(ROBANDPEACE_STEAL_COOLDOWN, 0L);
    }

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
            if (robandpeace$getStealCooldown() > 0 && robandpeace$getServerConfigSupplier().get().disableAttackingInCoolTime) {
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
        return !(source.getAttacker() instanceof PlayerEntity) || robandpeace$getStealCooldown() <= 0;
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
                case ElderGuardianEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case PiglinBruteEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case EvokerEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case WardenEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case WitherEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case EnderDragonEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                // normal mobs
                case MerchantEntity ignored -> 100;
                case HostileEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.hostile;
                default -> robandpeace$getServerConfigSupplier().get().stealChances.friendly;
            };
            if (source.robandpeace$isCritical()) {
                chance += robandpeace$getServerConfigSupplier().get().stealChances.criticalBonus;
            }
            var stack = source.getWeaponStack();
            if (stack != null && stack.getItem() instanceof SwordItem swordItem) {
                var toolBonus = switch (swordItem.getMaterial()) {
                    case ToolMaterials.WOOD -> robandpeace$getServerConfigSupplier().get().items.woodenGlove;
                    case ToolMaterials.STONE -> robandpeace$getServerConfigSupplier().get().items.stoneGlove;
                    case ToolMaterials.IRON -> robandpeace$getServerConfigSupplier().get().items.ironGlove;
                    case ToolMaterials.GOLD -> robandpeace$getServerConfigSupplier().get().items.goldenGlove;
                    case ToolMaterials.DIAMOND -> robandpeace$getServerConfigSupplier().get().items.diamondGlove;
                    case ToolMaterials.NETHERITE -> robandpeace$getServerConfigSupplier().get().items.netheriteGlove;
                    default -> 0;
                };
                chance += toolBonus;
            }
            var rand = entity.getRandom().nextInt(100);
            if (rand >= chance) {
                robandpeace$setStealCooldown(robandpeace$getServerConfigSupplier().get().stealCoolTime.onFailure);
                ci.cancel();
                return;
            }
            if (entity instanceof MerchantEntity merchant) {
                var weight = robandpeace$getServerConfigSupplier().get().stealChances.merchantTradeWeight;
                var offers = merchant.getOffers();
                ItemEntity itemEntity;
                if (!offers.isEmpty() && rand < weight) {
                    var randomOffer = offers.get(entity.getRandom().nextInt(offers.size()));
                    var sellItem = randomOffer.copySellItem();
                    itemEntity = merchant.dropStack(sellItem);
                } else {
                    var dropCount = entity.getRandom().nextInt(3);
                    itemEntity = merchant.dropStack(new ItemStack(Items.EMERALD, dropCount + 1));
                }
                if (itemEntity != null) {
                    itemEntity.setOwner(player.getUuid());
                }
            } else {
                dropLoot(source, true);
            }
            // playerHitTimerを0以上にしないとxpが落ちない
            playerHitTimer = 100;
            dropXp(player);
            robandpeace$setStealCooldown(robandpeace$getServerConfigSupplier().get().stealCoolTime.onSuccess);
            entity.getWorld().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ENTITY_CHICKEN_EGG, entity.getSoundCategory(), 1.0F, 1.1F);
            if (entity instanceof VillagerEntity villager && entity.getWorld() instanceof ServerWorld serverWorld) {
                var playerReputation = villager.getGossip().getReputationFor(player.getUuid(), (type) -> true);
                float golemCount = robandpeace$getGolemCount(playerReputation);
                var spawnRadius = robandpeace$getServerConfigSupplier().get().angryGolem.spawnRadius;
                var spawnDiameter = spawnRadius * 2;
                var spawnHeight = robandpeace$getServerConfigSupplier().get().angryGolem.spawnHeight;
                for (int i = 0; i < golemCount; i++) {
                    var dx = entity.getRandom().nextInt(spawnDiameter) - spawnRadius;
                    var dz = entity.getRandom().nextInt(spawnDiameter) - spawnRadius;
                    var spawnPos = villager.getBlockPos().add(dx, spawnHeight, dz);
                    RapEntityType.ANGRY_GOLEM.spawn(serverWorld, (g) -> {
                        g.setTarget(player);
                        g.getDataTracker().set(ROBANDPEACE_STEAL_COOLDOWN, 100000000L);
                    }, spawnPos, SpawnReason.MOB_SUMMONED, true, false);
                }
            }
            ci.cancel();
        }
    }

    @Unique
    private float robandpeace$getGolemCount(int playerReputation) {
        float golemCount = robandpeace$getServerConfigSupplier().get().angryGolem.maxSpawnCount;
        if (playerReputation > 0) {
            golemCount = golemCount / 5f;
        } else if (playerReputation > -25) {
            golemCount = golemCount * (2 / 5f);
        } else if (playerReputation > -50) {
            golemCount = golemCount * (3 / 5f);
        } else if (playerReputation > -75) {
            golemCount = golemCount * (4 / 5f);
        }
        return golemCount;
    }

    @Inject(
            method = "tick",
            at = @At("HEAD")
    )
    private void decreaseCooldown(CallbackInfo ci) {
        var stealCooldown = robandpeace$getStealCooldown();
        if (stealCooldown > 0) {
            robandpeace$setStealCooldown(stealCooldown - 1);
        }
    }

    @Override
    public long robandpeace$getStealCooldown() {
        var entity = (LivingEntity) (Object) this;
        return entity.getDataTracker().get(ROBANDPEACE_STEAL_COOLDOWN);
    }

    @Unique
    private void robandpeace$setStealCooldown(long value) {
        var entity = (LivingEntity) (Object) this;
        entity.getDataTracker().set(ROBANDPEACE_STEAL_COOLDOWN, value);
    }

    @Override
    public Supplier<RapServerConfig> robandpeace$getServerConfigSupplier() {
        return robandpeace$serverConfigSupplier;
    }

    @Override
    public void robandpeace$setServerConfigSupplier(Supplier<RapServerConfig> supplier) {
        robandpeace$serverConfigSupplier = supplier;
    }
}
