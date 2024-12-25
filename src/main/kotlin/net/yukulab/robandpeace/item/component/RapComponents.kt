package net.yukulab.robandpeace.item.component

import com.mojang.serialization.Codec
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID

object RapComponents {
    val SMOKE_INVISIBLE_DURATION: ComponentType<Int> = register("smoke_invisible_duration", ComponentType.builder<Int>().codec(Codec.INT))
    val PICKING_CHANCE: ComponentType<Int> = register("picking_chance", ComponentType.builder<Int>().codec(Codec.INT))
    val IS_OMEN: ComponentType<Boolean> = register("is_omen", ComponentType.builder<Boolean>().codec(Codec.BOOL))

    // Portal hoop components
    val PORTAL_HOOP_IS_REMOVE_MODE: ComponentType<Boolean> = register("portal_hoop_is_remove_mode", ComponentType.builder<Boolean>().codec(Codec.BOOL))
    val PORTAL_HOOP_LAST_USED: ComponentType<Long> = register("portal_hoop_last_used", ComponentType.builder<Long>().codec(Codec.LONG))
    val PORTAL_ID_ORIGIN: ComponentType<Int> = register("portal_id_origin", ComponentType.builder<Int>().codec(Codec.INT))
    val PORTAL_ID_DESTINATION: ComponentType<Int> = register("portal_id_destination", ComponentType.builder<Int>().codec(Codec.INT))

    private fun <T, C : ComponentType.Builder<T>> register(name: String, componentType: C): ComponentType<T> = register(name, componentType.build())

    private fun <T, C : ComponentType<T>> register(name: String, componentType: C): C = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(MOD_ID, name),
        componentType,
    )

    fun init() = Unit
}
