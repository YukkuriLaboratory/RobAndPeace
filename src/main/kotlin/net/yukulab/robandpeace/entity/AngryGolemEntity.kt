package net.yukulab.robandpeace.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.world.World
import net.yukulab.robandpeace.config.RapConfigs

class AngryGolemEntity(type: EntityType<AngryGolemEntity>, world: World) : IronGolemEntity(type, world) {
    private var lastTargetPlayer: PlayerEntity? = null
    private var livingTick = 0
    private val maxLivingTick = RapConfigs.serverConfig.angryGolem.liveTime

    init {
        chooseRandomAngerTime()
    }

    override fun tick() {
        super.tick()
        val targetPlayer = lastTargetPlayer
        if (target == null && targetPlayer != null && canTarget(targetPlayer) && canSee(targetPlayer) && !targetPlayer.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            angryAt = targetPlayer.uuid
            target = targetPlayer
        }
        if (++livingTick >= maxLivingTick) {
            discard()
        }
    }

    override fun chooseRandomAngerTime() {
        angerTime = RapConfigs.serverConfig.angryGolem.liveTime
    }

    override fun dropLoot(damageSource: DamageSource?, causedByPlayer: Boolean) {
        // Do not drop loot
    }

    override fun dropXp(attacker: Entity?) {
        // Do not drop XP
    }

    override fun setTarget(target: LivingEntity?) {
        super.setTarget(target)
        if (target is PlayerEntity) {
            lastTargetPlayer = target
        }
    }
}
