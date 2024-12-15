package net.yukulab.robandpeace.entity

import net.minecraft.entity.EntityType
import net.minecraft.world.World
import net.yukulab.robandpeace.DelegatedLogger
import qouteall.imm_ptl.core.portal.Portal

class ThroughHoopPortal(entityType: EntityType<*>, world: World) : Portal(entityType, world) {
    companion object {
        private val logger by DelegatedLogger()
    }
}
