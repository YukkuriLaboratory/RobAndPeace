package net.yukulab.robandpeace

import net.fabricmc.api.ModInitializer
import net.yukulab.robandpeace.item.RapItems

object RobAndPeace : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        RapItems.init()
    }
}
