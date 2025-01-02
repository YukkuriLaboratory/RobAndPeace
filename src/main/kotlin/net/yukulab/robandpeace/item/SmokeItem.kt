package net.yukulab.robandpeace.item

import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.Angerable
import net.minecraft.entity.mob.MobEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World
import net.yukulab.robandpeace.config.RapConfigs
import net.yukulab.robandpeace.extension.StanHolder
import net.yukulab.robandpeace.item.component.RapComponents

class SmokeItem(private val enableStan: Boolean = false) : Item(Settings()) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val random = world.random
        val pitch = if (enableStan) 1.5f else 2.0f
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
        if (enableStan) {
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
        val smokeCount = if (enableStan) {
            140
        } else {
            70
        }
        repeat(smokeCount) {
            fun nextSign() = if (random.nextBoolean()) 1 else -1
            val particleType = if (enableStan) {
                if (random.nextBoolean()) ParticleTypes.EXPLOSION else ParticleTypes.CAMPFIRE_COSY_SMOKE
            } else {
                ParticleTypes.CAMPFIRE_COSY_SMOKE
            }
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
        if (world is ServerWorld) {
            val duration = stack.getOrDefault(RapComponents.SMOKE_INVISIBLE_DURATION, RapConfigs.serverConfig.items.smokeInvisibleDuration)
            user.addStatusEffect(StatusEffectInstance(StatusEffects.INVISIBILITY, duration, 0, false, false))
            world.getEntitiesByClass(MobEntity::class.java, user.boundingBox.expand(12.0)) {
                it.isAlive && it.target == user || (it is Angerable && it.shouldAngerAt(user))
            }.forEach {
                it.target = null
                (it as? Angerable)?.stopAnger()
                if (enableStan && it is StanHolder) {
                    it.`robandpeace$SetStan`(duration)
                }
            }
        }

        user.incrementStat(Stats.USED.getOrCreateStat(this))
        stack.decrementUnlessCreative(1, user)
        return TypedActionResult.success(stack, world.isClient())
    }
}
