package net.yukulab.robandpeace.item

import net.minecraft.client.item.ClampedModelPredicateProvider
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.entity.effect.StatusEffects

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
        ModelPredicateProviderRegistry.register(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.KEY_PICKING, itemUsingDetector)
        ModelPredicateProviderRegistry.register(RapItems.TRIAL_PICKING_TOOL, PickingToolItem.KEY_OMINOUS) { _, _, livingEntity, _ ->
            if (livingEntity?.hasStatusEffect(StatusEffects.BAD_OMEN) == true) {
                1.0f
            } else {
                0.0f
            }
        }
    }
}
