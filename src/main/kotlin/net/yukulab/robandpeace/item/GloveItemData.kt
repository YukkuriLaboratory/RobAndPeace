package net.yukulab.robandpeace.item

import net.minecraft.item.Items
import net.minecraft.util.Identifier
import net.yukulab.robandpeace.MOD_ID

const val GLOVE_SUFFIX = "_glove"
val HAS_PLAYER: Identifier = Identifier.of(MOD_ID, "has_player")
val GLOVE_ITEMS = arrayOf(
    Items.WOODEN_SWORD,
    Items.STONE_SWORD,
    Items.IRON_SWORD,
    Items.GOLDEN_SWORD,
    Items.DIAMOND_SWORD,
    Items.NETHERITE_SWORD,
)
