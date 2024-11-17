package net.yukulab.robandpeace.entity

import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.passive.IronGolemEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.world.World

object RapEntityType {
    @JvmField
    val ANGRY_GOLEM = register("angry_golem", ::AngryGolemEntity, SpawnGroup.AMBIENT) {
        disableSaving()
        dimensions(1.4f, 2.7f)
        maxTrackingRange(10)
    }

    private fun <E : Entity> register(id: String, initializer: (EntityType<E>, World) -> E, group: SpawnGroup, builder: EntityType.Builder<E>.() -> Unit): EntityType<E> = Registry.register(Registries.ENTITY_TYPE, id, EntityType.Builder.create(initializer, group).apply(builder).build(id))

    fun init() {
        FabricDefaultAttributeRegistry.register(ANGRY_GOLEM, IronGolemEntity.createIronGolemAttributes())
    }
}
