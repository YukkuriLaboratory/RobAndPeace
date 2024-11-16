package net.yukulab.robandpeace.item

import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID

object RapItems {

    // val EXAMPLE_ITEM = register("example_item", Item(Item.Settings()))

    private fun <T : Item> register(id: String, item: T): T = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, id), item)

    // Dummy init function to make sure the object is loaded
    fun init() = Unit
}
