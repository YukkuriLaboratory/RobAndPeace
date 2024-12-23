package net.yukulab.robandpeace

import net.fabricmc.api.ModInitializer
import net.yukulab.robandpeace.config.RapConfigs
import net.yukulab.robandpeace.entity.RapEntityType
import net.yukulab.robandpeace.item.RapItems
import net.yukulab.robandpeace.item.component.RapComponents
import net.yukulab.robandpeace.network.RabNetworking

object RobAndPeace : ModInitializer {
    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        RapConfigs.init()
        RapComponents.init()
        RapItems.init()
        RapEntityType.init()
        RabNetworking.init()
    }
}
