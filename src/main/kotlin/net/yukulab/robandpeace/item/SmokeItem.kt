package net.yukulab.robandpeace.item

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.particle.SimpleParticleType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.yukulab.robandpeace.config.RapConfigs
import net.yukulab.robandpeace.coroutineScope
import net.yukulab.robandpeace.extension.SmokeEffectHolder
import net.yukulab.robandpeace.extension.ticks
import net.yukulab.robandpeace.item.component.RapComponents
import net.yukulab.robandpeace.serverDispatcher

class SmokeItem(private val type: Type) : Item(Settings()) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        if (world is ServerWorld && user is ServerPlayerEntity) {
            playEffects(world, user)
            val duration = stack.getOrDefault(RapComponents.SMOKE_INVISIBLE_DURATION, RapConfigs.serverConfig.items.smokeInvisibleDuration)
            world.getEntitiesByClass(MobEntity::class.java, user.boundingBox.expand(12.0)) {
                it.isAlive && it.target == user || (it is Angerable && it.shouldAngerAt(user))
            }.forEach {
                it.target = null
                (it as? Angerable)?.stopAnger()
                when (type) {
                    Type.EXPLOSION -> if (it is SmokeEffectHolder) {
                        it.`robandpeace$SetStan`(duration)
                    }
                    Type.FIRE -> if (it is SmokeEffectHolder) {
                        val delay = RapConfigs.serverConfig.items.fireSmokeEffectDelay
                        it.`robandpeace$SetDiscard`(delay)
                    }
                    else -> {}
                }
            }
            if (type == Type.NORMAL) {
                user.addStatusEffect(StatusEffectInstance(StatusEffects.INVISIBILITY, duration, 0, false, false))
            }
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this))
        stack.decrementUnlessCreative(1, user)
        return TypedActionResult.success(stack, world.isClient())
    }

    private fun playEffects(
        world: ServerWorld,
        user: ServerPlayerEntity,
    ) {
        val pitch = when (type) {
            Type.NORMAL -> 2.0f
            Type.EXPLOSION -> 1.5f
            Type.FIRE -> 1.3f
        }
        world.playSound(
            null,
            user.x,
            user.y,
            user.z,
            SoundEvents.BLOCK_FIRE_EXTINGUISH,
            SoundCategory.NEUTRAL,
            0.5f,
            pitch,
        )
        when (type) {
            Type.EXPLOSION -> {
                world.playSound(
                    null,
                    user.x,
                    user.y,
                    user.z,
                    SoundEvents.ENTITY_GENERIC_EXPLODE,
                    SoundCategory.NEUTRAL,
                    0.3f,
                    1.3f,
                )
            }
            Type.FIRE -> {
                val delay = RapConfigs.serverConfig.items.fireSmokeEffectDelay
                coroutineScope.launch {
                    delay(delay.ticks)
                    withContext(serverDispatcher) {
                        world.playSound(
                            null,
                            user.x,
                            user.y,
                            user.z,
                            SoundEvents.ENTITY_GHAST_SHOOT,
                            SoundCategory.NEUTRAL,
                            0.3f,
                            1.3f,
                        )
                        playParticle(70, world, ParticleTypes.FLAME, user)
                    }
                }
            }
            else -> {}
        }
        val smokeCount = when (type) {
            Type.NORMAL -> 80
            Type.EXPLOSION -> 140
            Type.FIRE -> 80
        }
        when (type) {
            Type.NORMAL -> {
                playParticle(smokeCount, world, ParticleTypes.CAMPFIRE_COSY_SMOKE, user)
            }
            Type.EXPLOSION -> {
                playParticle(smokeCount / 2, world, ParticleTypes.CAMPFIRE_COSY_SMOKE, user)
                playParticle(smokeCount / 2, world, ParticleTypes.EXPLOSION, user)
            }
            Type.FIRE -> {
                playParticle(smokeCount, world, ParticleTypes.CAMPFIRE_COSY_SMOKE, user)
            }
        }
    }

    private fun playParticle(
        count: Int,
        world: ServerWorld,
        particleType: SimpleParticleType?,
        user: ServerPlayerEntity,
    ) {
        world.spawnParticles(
            user,
            particleType,
            false,
            user.x,
            user.y + 1.1,
            user.z,
            count,
            0.66,
            0.52,
            0.66,
            0.001,
        )
    }

    enum class Type {
        NORMAL,
        EXPLOSION,
        FIRE,
    }
}
