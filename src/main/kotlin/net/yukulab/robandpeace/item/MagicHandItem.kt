package net.yukulab.robandpeace.item

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.minecraft.component.type.AttributeModifierSlot
import net.minecraft.component.type.AttributeModifiersComponent
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.attribute.EntityAttributeModifier
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID

class MagicHandItem(range: Double, maxDamage: Int) :
    Item(
        Settings().attributeModifiers(
            AttributeModifiersComponent.builder()
                .add(
                    EntityAttributes.PLAYER_BLOCK_INTERACTION_RANGE,
                    EntityAttributeModifier(Identifier.of(MOD_ID, "magic_hand"), range, EntityAttributeModifier.Operation.ADD_VALUE),
                    AttributeModifierSlot.HAND,
                )
                .build(),
        ).maxCount(1).maxDamage(maxDamage),
    ) {
    companion object {
        init {
            UseBlockCallback.EVENT.register { player, _, _, _ ->
                consumeMagicHandIfEquipped(player)
                ActionResult.PASS
            }
            PlayerBlockBreakEvents.AFTER.register { _, player, _, _, _ ->
                consumeMagicHandIfEquipped(player)
            }
        }

        private fun consumeMagicHandIfEquipped(player: PlayerEntity) {
            listOf(EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND)
                .associateWith { player.getEquippedStack(it) }
                .filterValues { it.item is MagicHandItem }
                .forEach { (hand, magicHand) ->
                    magicHand.damage(1, player, hand)
                }
        }
    }
}
