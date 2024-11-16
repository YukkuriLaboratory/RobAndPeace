package net.yukulab.robandpeace

import net.fabricmc.api.ModInitializer

object RobAndPeace : ModInitializer {
    private val logger by DelegatedLogger()

    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        logger.info("Hello Fabric world!")
    }
}
