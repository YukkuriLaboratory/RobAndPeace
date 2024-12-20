package net.yukulab.robandpeace.entity

import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.EntityType
import net.minecraft.world.World

class HiddenTreasureEntity(entityType: EntityType<out AreaEffectCloudEntity>, world: World) : AreaEffectCloudEntity(entityType, world) {
    var currentTick: Int = 0

    override fun tick() {
        super.tick()
        currentTick++
        if (currentTick > 100) {
            kill()
        }
    }
}
