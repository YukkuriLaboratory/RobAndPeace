package net.yukulab.robandpeace.item

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID

object RapItems {

    val SMOKE: SmokeItem = register("smoke", SmokeItem())
    val ADVANCED_SMOKE: SmokeItem = register("advanced_smoke", SmokeItem(enableStan = true))
    val SPIDER_WALKER: SpiderWalkerItem = register("spider_walker", SpiderWalkerItem())
    val MAGIC_HAND: MagicHandItem = register("magic_hand", MagicHandItem(5.0, 131))
    val ADVANCED_MAGIC_HAND: MagicHandItem = register("advanced_magic_hand", MagicHandItem(8.0, 1561))
    val PICKING_TOOL: PickingToolItem = register("picking_tool", PickingToolItem(50))
    val TRIAL_PICKING_TOOL: PickingToolItem = register("trial_picking_tool", PickingToolItem(70, true))
    val PORTAL_HOOP: PortalHoopItem = register("portal_hoop", PortalHoopItem())
    val HIDDEN_TREASURE: HiddenTreasureItem = register("hidden_treasure", HiddenTreasureItem())

    private fun <T : Item> register(id: String, item: T): T = Registry.register(Registries.ITEM, Identifier.of(MOD_ID, id), item)

    val ITEM_GROUP_KEY: RegistryKey<ItemGroup> = RegistryKey.of(Registries.ITEM_GROUP.key, Identifier.of(MOD_ID, "rap_item_group"))
    private val ITEM_GROUP: ItemGroup = FabricItemGroup.builder()
        .icon { ItemStack(SMOKE) }
        .displayName(Text.translatable("itemGroup.robandpeace"))
        .build()

    fun init() {
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP_KEY, ITEM_GROUP)
        ItemGroupEvents.modifyEntriesEvent(ITEM_GROUP_KEY).register {
            it.add(SMOKE)
            it.add(ADVANCED_SMOKE)
            it.add(SPIDER_WALKER)
            it.add(MAGIC_HAND)
            it.add(ADVANCED_MAGIC_HAND)
            it.add(PICKING_TOOL)
            it.add(TRIAL_PICKING_TOOL)
            it.add(PORTAL_HOOP)
            it.add(HIDDEN_TREASURE)
        }
    }
}
