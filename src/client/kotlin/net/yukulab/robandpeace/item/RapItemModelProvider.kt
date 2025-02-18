package net.yukulab.robandpeace.item

import net.minecraft.client.item.ClampedModelPredicateProvider
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.entity.player.PlayerEntity
import net.yukulab.robandpeace.item.component.RapComponents

object RapItemModelProvider {
    fun registerModelPredicateProviders() {
        val itemUsingDetector = ClampedModelPredicateProvider { itemStack, _, livingEntity, _ ->
            if (livingEntity?.activeItem == itemStack) {
                1.0f
            } else {
                0.0f
            }
        }
        ModelPredicateProviderRegistry.register(RapItems.PICKING_TOOL, PickingToolItem.KEY_PICKING, itemUsingDetector)
        ModelPredicateProviderRegistry.register(RapItems.OMINOUS_PICKING_TOOL, PickingToolItem.KEY_PICKING, itemUsingDetector)
        ModelPredicateProviderRegistry.register(RapItems.OMINOUS_PICKING_TOOL, PickingToolItem.KEY_TRIAL) { itemStack, _, _, _ ->
            if (itemStack[RapComponents.IS_OMEN] == true) {
                1.0f
            } else {
                0.0f
            }
        }

        // Portal Hoop
        ModelPredicateProviderRegistry.register(RapItems.PORTAL_HOOP, PortalHoopItem.KEY_REMOVE_MODE) { stack, _, _, _ ->
            if (stack.get(RapComponents.PORTAL_HOOP_IS_REMOVE_MODE) == true) {
                1.0f
            } else {
                0.0f
            }
        }

        // Gloves
        GLOVE_ITEMS.forEach {
            ModelPredicateProviderRegistry.register(it, HAS_PLAYER) { _, _, entity, _ ->
                if (entity is PlayerEntity || entity == null) {
                    1.0f
                } else {
                    0.0f
                }
            }
        }
    }
}
