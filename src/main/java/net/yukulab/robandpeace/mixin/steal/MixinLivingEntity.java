package net.yukulab.robandpeace.mixin.steal;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
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
import net.minecraft.item.*;
import net.minecraft.registry.RegistryKeys;
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
public abstract class MixinLivingEntity extends Entity implements StealCooldownHolder, RapConfigInjector {
    @Unique
    private static TrackedData<Long> ROBANDPEACE_STEAL_COOLDOWN;

    @Unique
    private Supplier<RapServerConfig> robandpeace$serverConfigSupplier = RapConfigs::getServerConfig;

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    protected abstract void dropLoot(DamageSource damageSource, boolean causedByPlayer);

    @Shadow
    protected abstract void dropXp(@Nullable Entity attacker);

    @Shadow
    protected int playerHitTimer;

    @Shadow
    @Final
    private static Logger LOGGER;
    @Unique
    private boolean robandpeace$isItemDropped = false;

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

    @WrapWithCondition(
            method = "onDeath",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;drop(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/entity/damage/DamageSource;)V")
    )
    private boolean disableItemDropOnDeathForMobEntity(LivingEntity instance, ServerWorld world, DamageSource damageSource) {
        return !(instance instanceof MobEntity);
    }

    @Override
    public @Nullable ItemEntity dropStack(ItemStack stack, float yOffset) {
        var result = super.dropStack(stack, yOffset);
        if (result != null) {
            robandpeace$isItemDropped = true;
        }
        return result;
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
            var baseChance = 1;
            var multiply = switch (entity) {
                // bosses
                case ElderGuardianEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case PiglinBruteEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case EvokerEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case WardenEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case WitherEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.boss;
                case EnderDragonEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.enderDragon;
                // normal mobs
                case MerchantEntity ignored -> 100;
                case HostileEntity ignored -> robandpeace$getServerConfigSupplier().get().stealChances.hostile;
                default -> robandpeace$getServerConfigSupplier().get().stealChances.friendly;
            };
            if (source.robandpeace$isCritical()) {
                baseChance += robandpeace$getServerConfigSupplier().get().stealChances.criticalBonus;
            }
            var stack = source.getWeaponStack();
            if (stack != null) {
                switch (stack.getItem()) {
                    case SwordItem swordItem -> {
                        var toolBonus = switch (swordItem.getMaterial()) {
                            case ToolMaterials.WOOD -> robandpeace$getServerConfigSupplier().get().items.woodenGlove;
                            case ToolMaterials.STONE -> robandpeace$getServerConfigSupplier().get().items.stoneGlove;
                            case ToolMaterials.IRON -> robandpeace$getServerConfigSupplier().get().items.ironGlove;
                            case ToolMaterials.GOLD -> robandpeace$getServerConfigSupplier().get().items.goldenGlove;
                            case ToolMaterials.DIAMOND ->
                                    robandpeace$getServerConfigSupplier().get().items.diamondGlove;
                            case ToolMaterials.NETHERITE ->
                                    robandpeace$getServerConfigSupplier().get().items.netheriteGlove;
                            default -> 0;
                        };
                        baseChance += toolBonus;
                    }
                    case RangedWeaponItem ignored -> baseChance = 0;
                    default -> {
                    }
                }
                var lootingLevel = getRegistryManager().getOptionalWrapper(RegistryKeys.ENCHANTMENT)
                        .flatMap((registry) -> registry.getOptional(Enchantments.LOOTING))
                        .map((looting) -> stack.getEnchantments().getLevel(looting))
                        .orElse(0);
                baseChance += lootingLevel;
            }
            var rand = entity.getRandom().nextInt(10000) * 0.01;
            var chance = baseChance * multiply;
            LOGGER.debug("Steal chance: {}% (base: {}%, multiply: {})", chance, baseChance, multiply);
            LOGGER.debug("Result: {}", rand);
            if (rand >= chance) {
                LOGGER.debug("Steal failed");
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
            } else if (entity instanceof EnderDragonEntity dragon) {
                dragon.setHealth(0);
            } else {
                // スリ取り成功時は必ずアイテムをドロップしてほしいが、そもそもアイテムをドロップしないMOBの可能性も考えて7万回まで施行するようにしておく
                // 例えばドロップ率が0.001%のMOBでも7万回連続でドロップしない確立は0.09%程度になる
                var millis = System.currentTimeMillis();
                for (int i = 0; i < 70000; i++) {
                    dropLoot(source, true);
                    if (robandpeace$isItemDropped) {
                        break;
                    }
                }
                // 紙魚などでフルに試行されてしまうと初回のみ40ms程度かかり、その後最適化されるのか2回目以降は20ms以下に収まる
                LOGGER.debug("Drop time: {}ms", System.currentTimeMillis() - millis);
                robandpeace$isItemDropped = false;
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
