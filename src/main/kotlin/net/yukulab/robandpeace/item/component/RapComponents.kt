package net.yukulab.robandpeace.item.component

import com.mojang.serialization.Codec
import net.minecraft.component.ComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID

object RapComponents {
    val SMOKE_INVISIBLE_DURATION = register("smoke_invisible_duration", ComponentType.builder<Int>().codec(Codec.INT))

    private fun <T, C : ComponentType.Builder<T>> register(name: String, componentType: C): ComponentType<T> = register(name, componentType.build())

    private fun <T, C : ComponentType<T>> register(name: String, componentType: C): C = Registry.register(
        Registries.DATA_COMPONENT_TYPE,
        Identifier.of(MOD_ID, name),
        componentType,
    )

    fun init() = Unit
}
