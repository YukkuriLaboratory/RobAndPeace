package net.yukulab.robandpeace.item

import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.particle.SimpleParticleType
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.random.Random
import net.minecraft.world.World
import net.yukulab.robandpeace.config.RapConfigs
import net.yukulab.robandpeace.coroutineScope
import net.yukulab.robandpeace.extension.SmokeEffectHolder
import net.yukulab.robandpeace.item.component.RapComponents
import net.yukulab.robandpeace.serverDispatcher

class SmokeItem(private val type: Type) : Item(Settings()) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        playEffects(world, user)
        if (world is ServerWorld) {
            val duration = stack.getOrDefault(RapComponents.SMOKE_INVISIBLE_DURATION, RapConfigs.serverConfig.items.smokeInvisibleDuration)
            user.addStatusEffect(StatusEffectInstance(StatusEffects.INVISIBILITY, duration, 0, false, false))
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
                        it.`robandpeace$SetDiscard`(20)
                    }
                    else -> {}
                }
            }
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this))
        stack.decrementUnlessCreative(1, user)
        return TypedActionResult.success(stack, world.isClient())
    }

    private fun playEffects(
        world: World,
        user: PlayerEntity,
    ) {
        val random = world.random
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
                coroutineScope.launch {
                    delay(1.seconds)
                    launch(serverDispatcher) {
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
                        repeat(70) {
                            playParticle(random, world, ParticleTypes.FLAME, user)
                        }
                    }
                }
            }
            else -> {}
        }
        val smokeCount = when (type) {
            Type.NORMAL -> 70
            Type.EXPLOSION -> 140
            Type.FIRE -> 70
        }
        repeat(smokeCount) {
            val particleType = when (type) {
                Type.NORMAL -> ParticleTypes.CAMPFIRE_COSY_SMOKE
                Type.EXPLOSION -> if (random.nextBoolean()) ParticleTypes.EXPLOSION else ParticleTypes.CAMPFIRE_COSY_SMOKE
                Type.FIRE -> ParticleTypes.CAMPFIRE_COSY_SMOKE
            }
            playParticle(random, world, particleType, user)
        }
    }

    private fun playParticle(
        random: Random,
        world: World,
        particleType: SimpleParticleType?,
        user: PlayerEntity,
    ) {
        fun nextSign() = if (random.nextBoolean()) 1 else -1
        world.addParticle(
            particleType,
            user.x + 1 * random.nextDouble() * nextSign(),
            user.y + 2 * random.nextDouble(),
            user.z + 1 * random.nextDouble() * nextSign(),
            0.01 * nextSign(),
            0.02,
            0.01 * nextSign(),
        )
    }

    enum class Type {
        NORMAL,
        EXPLOSION,
        FIRE,
    }
}
