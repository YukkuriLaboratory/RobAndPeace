package net.yukulab.robandpeace.entity

import net.minecraft.entity.EntityType
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.world.World
import net.yukulab.robandpeace.config.RapConfigs

class AngryGolemEntity(type: EntityType<AngryGolemEntity>, world: World) : IronGolemEntity(type, world) {
    init {
        chooseRandomAngerTime()
    }

    override fun tick() {
        super.tick()
        if (!hasAngerTime()) {
            discard()
        }
    }

    override fun chooseRandomAngerTime() {
        angerTime = RapConfigs.serverConfig.angryGolemLiveTime
    }
}
