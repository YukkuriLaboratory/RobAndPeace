package net.yukulab.robandpeace.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

class HiddenTreasureEntity(entityType: EntityType<HiddenTreasureEntity>, world: World) : Entity(entityType, world) {
    var currentTick: Int = 0
    var owner: PlayerEntity? = null

    override fun tick() {
        super.tick()
        owner?.syncedPos?.add(0.0, 1.0, 0.0)?.also(::setPosition)
        currentTick++
        if (currentTick > DRAGON_DEATH_DURATION) {
            kill()
        }
    }

    override fun initDataTracker(builder: DataTracker.Builder?) {
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound?) {
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound?) {
    }

    override fun shouldSave(): Boolean = false

    companion object {
        /**
         * [net.minecraft.entity.boss.dragon.EnderDragonEntity.updatePostDeath]#L580
         */
        const val DRAGON_DEATH_DURATION = 200
    }
}
